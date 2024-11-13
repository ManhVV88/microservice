package com.microservice.walletservice.service.impl;

import com.microservice.commonbase.event.dto.NotificationEvent;
import com.microservice.walletservice.dto.request.DonateToPostRequest;
import com.microservice.walletservice.dto.request.WalletUpdateRequest;
import com.microservice.walletservice.dto.response.BalanceResponse;
import com.microservice.walletservice.dto.response.WalletUserResponse;
import com.microservice.walletservice.entity.DetailWalletUser;
import com.microservice.walletservice.entity.WalletUser;
import com.microservice.walletservice.exception.ErrorCode;
import com.microservice.walletservice.exception.WalletException;
import com.microservice.walletservice.mapper.WalletUserMapper;
import com.microservice.walletservice.repository.WalletUserRepository;
import com.microservice.walletservice.repository.httpClient.IdentityClient;
import com.microservice.walletservice.repository.httpClient.PostClient;
import com.microservice.walletservice.service.WalletUserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Slf4j
public class WalletUserServiceImpl implements WalletUserService {

    WalletUserRepository walletUserRepository;
    IdentityClient identityClient;
    PostClient postClient;
    WalletUserMapper walletUserMapper;
    KafkaTemplate<String, Object> kafkaTemplate;
    RedisTemplate<String, Object> redisTemplate;
    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @NonFinal
    String lockKey;

    @Override
    public WalletUserResponse getWalletUser() {
        return walletUserMapper.toWalletUserResponse(
                walletUserRepository.findByUserId(
                        identityClient.getUserId("").getResult()
                                .get(SecurityContextHolder.getContext().getAuthentication().getName())
                                .getUserId()
                ).orElseThrow(() -> new WalletException(ErrorCode.INVALID_NOT_FOUND))
        );
    }

    @Override
    @Transactional
    public WalletUserResponse updateWalletUser(WalletUpdateRequest request) {
        var userResponse =identityClient.getUserId("").getResult();
        if(userResponse == null){
            throw new WalletException(ErrorCode.INVALID_NOT_FOUND);
        }
        var userId = userResponse.get(SecurityContextHolder.getContext().getAuthentication().getName()).getUserId();
        var quantityRequest = request.getQuantity();
        var itemTypeRequest = request.getItemType();
        var walletUser = walletUserRepository.findByUserId(userId)
                .orElseGet(() -> WalletUser.builder()
                        .userId(userId)
                        .build());

        BalanceResponse balanceResponse = BalanceResponse.builder().build();
        walletUser.getListDetailWallet().stream()
                .filter(item->item.getItemType().equals(itemTypeRequest))
                .findFirst()
                .ifPresentOrElse(
                        item -> {
                            var quantityOld = item.getQuantity();
                            var quantityNew = quantityRequest + quantityOld;
                            balanceResponse.setPreviousBalance(quantityOld);
                            balanceResponse.setCurrentBalance(quantityNew);
                            item.setQuantity(quantityNew);
                        },
                        () -> {
                            balanceResponse.setPreviousBalance(0L);
                            balanceResponse.setCurrentBalance(quantityRequest);
                            walletUser.getListDetailWallet().add(
                                    DetailWalletUser.builder()
                                            .walletUser(walletUser)
                                            .itemType(request.getItemType())
                                            .quantity(request.getQuantity())
                                            .build());
                        }
                );

        WalletUserResponse walletUserResponse = walletUserMapper
                .toWalletUserResponse(walletUserRepository.save(walletUser));
        walletUserResponse.setBalanceResponse(balanceResponse);

        kafkaTemplate.send("notification-event", NotificationEvent.builder()
                .channel("EMAIL")
                .recipient(SecurityContextHolder.getContext().getAuthentication().getName())
                .subject("Wallet add point")
                .body(STR."Your wallet just received  \{quantityRequest} \{itemTypeRequest} points.")
                .build());

        return walletUserResponse;
    }

    @Override
    @Transactional
    public String donateToPostUser(DonateToPostRequest request) {

        var postResponse = postClient.getPost(request.getPostId()).getResult();
        if (postResponse == null) throw new WalletException(ErrorCode.INVALID_POST_NOT_FOUND);

        String authorEmail = postResponse.getAuthorEmail();
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        var listUserId = identityClient.getUserId(authorEmail).getResult();
        var currentUserId = listUserId.get(currentUserEmail).getUserId();
        var donateUserId = listUserId.get(authorEmail).getUserId();

        if(currentUserId == null || donateUserId == null) throw new WalletException(ErrorCode.INVALID_USER_NOT_FOUND);

        lockKey = STR."donate-lock:\{currentUserId}";
        Boolean lockAcquired = redisTemplate.opsForValue().setIfAbsent(lockKey, "locked", Duration.ofSeconds(10));

        if (Boolean.TRUE.equals(lockAcquired)) {
            try {
                // run scheduler to update TTL for lockKey every 8 seconds continue until request ends

                scheduler.scheduleAtFixedRate(this::renewLock, 0, 8, TimeUnit.SECONDS);
                redisTemplate.execute(new SessionCallback<>() {
                    @Override
                    public Object execute(@NonNull RedisOperations operations) {
                        operations.watch(lockKey);
                        operations.multi();
                        performDonation(currentUserId, donateUserId, authorEmail, request);
                        return operations.exec();
                    }
                });
            } catch (DataAccessException e) {
                log.error("Error while trying to lock locked for user : {}", currentUserId);
                log.error("Exception transaction : {}", e.getMessage());
            }
            finally {
                // delete lockKye and stop  scheduler
                redisTemplate.delete(lockKey);
            }
        } else {
            throw new WalletException(ErrorCode.REQUEST_CONFLICT);
        }
        return STR."success donate to \{authorEmail}";
    }


    private void renewLock() {
        // plus time to live for key
        redisTemplate.expire(lockKey, Duration.ofSeconds(10));
    }
    private void performDonation(
            String currentUserId,
            String donateUserId,
            String authorEmail,
            DonateToPostRequest request) {
        // get or create wallet if not present
        Map<String, WalletUser> walletUsers = walletUserRepository.findByUserIdOrUserId(currentUserId,donateUserId)
                .stream()
                .collect(Collectors.toMap(WalletUser::getUserId, walletUser -> walletUser));



        // get wallet of a user
        WalletUser walletCurrentUser = walletUsers.get(currentUserId);
        if(walletCurrentUser == null){
            throw new WalletException(ErrorCode.INVALID_QUANTITY);
        }
        walletCurrentUser.setListDetailWallet(
                walletCurrentUser.getListDetailWallet()
                        .stream()
                        .filter(detailWalletUser -> detailWalletUser.getItemType().equals(request.getItemType()))
                        .findFirst()
                        .map(detailWalletUser -> {
                            if(detailWalletUser.getQuantity() < request.getQuantity()
                                    || detailWalletUser.getQuantity().equals(0L))
                                throw new WalletException(ErrorCode.INVALID_QUANTITY);
                            detailWalletUser.setQuantity(detailWalletUser.getQuantity() - request.getQuantity());
                            return walletCurrentUser.getListDetailWallet();
                        })
                        .orElseThrow(() -> new WalletException(ErrorCode.INVALID_QUANTITY)));

        WalletUser walletUserDonated = walletUsers.computeIfAbsent(donateUserId, id -> {
                WalletUser tempWalletUserDonated2 = walletUserRepository.findByUserId(id).orElse(null);
                if (tempWalletUserDonated2 == null) {
                    tempWalletUserDonated2 = new WalletUser();
                    tempWalletUserDonated2.setUserId(id);
                    tempWalletUserDonated2.setListDetailWallet(new HashSet<>());
                    tempWalletUserDonated2.setCreatedAt(ZonedDateTime.now());
                    tempWalletUserDonated2.setUpdatedAt(ZonedDateTime.now());
                    tempWalletUserDonated2.setCreatedBy(authorEmail);
                    tempWalletUserDonated2.setUpdatedBy(authorEmail);
                }
                return tempWalletUserDonated2;
        });

        Set<DetailWalletUser> detailWalletUsers = walletUserDonated.getListDetailWallet();
        walletUserDonated.setListDetailWallet(
                detailWalletUsers.stream()
                        .filter(detailWalletUser -> detailWalletUser.getItemType().equals(request.getItemType()))
                        .findFirst()
                        .map(detailWalletUser -> {
                            detailWalletUser.setQuantity(detailWalletUser.getQuantity() + request.getQuantity());
                            return detailWalletUsers;
                        })
                        .orElseGet(() -> {
                            DetailWalletUser newDetailWalletUser = new DetailWalletUser();
                            newDetailWalletUser.setItemType(request.getItemType());
                            newDetailWalletUser.setQuantity(request.getQuantity());
                            newDetailWalletUser.setCreatedAt(ZonedDateTime.now());
                            newDetailWalletUser.setUpdatedAt(ZonedDateTime.now());
                            newDetailWalletUser.setCreatedBy(authorEmail);
                            newDetailWalletUser.setUpdatedBy(authorEmail);
                            newDetailWalletUser.setWalletUser(walletUserDonated);
                            detailWalletUsers.add(newDetailWalletUser);
                            return detailWalletUsers;
                        })
        );

        walletUserRepository.save(walletCurrentUser);
        walletUserRepository.save(walletUserDonated);
    }
}
