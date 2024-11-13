package com.microservice.walletservice.service.impl;

import com.microservice.walletservice.constant.Common;
import com.microservice.walletservice.constant.PriceForItem;
import com.microservice.walletservice.dto.request.CreateRechargeRequest;
import com.microservice.walletservice.dto.request.UpdateStatusRechargeRequest;
import com.microservice.walletservice.dto.response.CreateRechargeResponse;
import com.microservice.walletservice.dto.response.GetRechargeIdResponse;
import com.microservice.walletservice.entity.RechargePointHistory;
import com.microservice.walletservice.exception.ErrorCode;
import com.microservice.walletservice.exception.WalletException;
import com.microservice.walletservice.mapper.RechargeMapper;
import com.microservice.walletservice.repository.RechargePointHistoryRepository;
import com.microservice.walletservice.repository.httpClient.IdentityClient;
import com.microservice.walletservice.service.RechargePointService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class RechargePointServiceImpl implements RechargePointService {

    IdentityClient identityClient;
    RechargeMapper rechargeMapper;
    RechargePointHistoryRepository rechargePointHistoryRepository;

    @Override
    public CreateRechargeResponse createRechargePoint(CreateRechargeRequest createRechargeRequest) {
        var user = identityClient.getUserId("");
        if(user.getResult() == null) throw new WalletException(ErrorCode.INVALID_USER_NOT_FOUND);
        var priceForItem = PriceForItem.valueOf(createRechargeRequest.getTypeItem());
        var email = SecurityContextHolder.getContext().getAuthentication().getName();
        RechargePointHistory history = RechargePointHistory.builder()
                .amount(priceForItem.getPriceForItem())
                .email(email)
                .currency(priceForItem.getCurrencyCode())
                .quantity(createRechargeRequest.getPackageQuantity().getQuantity())
                .status(Common.Status.PENDING)
                .paymentMethod(Common.PaymentMethod.PAYPAL)
                .walletItemType(priceForItem.getWalletItemType())
                .userId(user.getResult().get(email).getUserId())
                .transactionDate(Instant.now())
                .build();

        return rechargeMapper.toRechargeResponse(rechargePointHistoryRepository.save(history));
    }

    @Override
    public GetRechargeIdResponse getRechargeIdHistory(String idHistory) {
        return rechargeMapper.toGetRechargeIdResponse(rechargePointHistoryRepository
                .findByIdAndStatusAndEmail(
                        idHistory,
                        Common.Status.PENDING,
                        SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new WalletException(ErrorCode.INVALID_NOT_FOUND)));
    }

    @Override
    public String updateStatusRecharge(UpdateStatusRechargeRequest request) {
        RechargePointHistory rechargePointHistory = rechargePointHistoryRepository
                .findByIdAndStatusAndEmail(
                        request.getRechargeId(),
                        Common.Status.PENDING,
                        SecurityContextHolder.getContext().getAuthentication().getName()
                )
                .orElseThrow(() -> new WalletException(ErrorCode.INVALID_NOT_FOUND));
        if(rechargePointHistory.getStatus().equals(Common.Status.SUCCESS)) {
            log.error("status update invalid : {}", rechargePointHistory.getStatus());
            throw new WalletException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
        rechargePointHistory.setStatus(request.getStatus());
        rechargePointHistory.setTransactionId(request.getGatewayTransactionId());
        rechargePointHistoryRepository.save(rechargePointHistory);
        return rechargePointHistory.getId();
    }

    @Override
    public List<GetRechargeIdResponse> getRechargeIdByEmailAndStatusPending(String email) {
        return rechargePointHistoryRepository.findByStatusAndEmail(Common.Status.PENDING,email)
                .stream().map(rechargeMapper::toGetRechargeIdResponse)
                .toList();
    }


}
