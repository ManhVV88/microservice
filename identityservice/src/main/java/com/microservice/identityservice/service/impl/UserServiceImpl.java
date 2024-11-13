package com.microservice.identityservice.service.impl;

import com.microservice.commonbase.event.dto.NotificationEvent;
import com.microservice.identityservice.constant.CRole;
import com.microservice.identityservice.constant.LevelRole;
import com.microservice.identityservice.dto.ListUserResponse;
import com.microservice.identityservice.dto.request.UserRequest;
import com.microservice.identityservice.dto.request.UserUpdatedRequest;
import com.microservice.identityservice.dto.response.UserIdResponse;
import com.microservice.identityservice.dto.response.UserResponse;
import com.microservice.identityservice.entity.Role;
import com.microservice.identityservice.entity.User;
import com.microservice.identityservice.exception.ErrorCode;
import com.microservice.identityservice.exception.IdentityException;
import com.microservice.identityservice.mapper.UserMapper;
import com.microservice.identityservice.repository.RoleRepository;
import com.microservice.identityservice.repository.UserRepository;
import com.microservice.identityservice.service.IUserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;
    KafkaTemplate<String, Object> kafkaTemplate;


    @Override
    public UserResponse createUser(UserRequest userRequest) {
        User user = userMapper.toUser(userRequest);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        HashSet<Role> roles = new HashSet<>();
        roleRepository.findById(CRole.USER_ROLE).ifPresent(roles::add); //ifPresent nếu có thì add ko thì ko làm gì

        user.setRoles(roles);
        userRepository.save(user);

        //push notificationEvent to topic "notification-event"
        kafkaTemplate.send("notification-event", NotificationEvent.builder()
                        .channel("EMAIL")
                        .recipient(user.getEmail())
                        .subject("Welcome to Microservice")
                        .body("Hello "+user.getName()+" , you are registered successfully")
                .build());

        return userMapper.toUserResponse(user);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ListUserResponse<UserResponse> getAllUsers(int page, int size) {

        Sort sort = Sort.by(Sort.Direction.ASC, "createdAt");
        Pageable pageable = PageRequest.of(page, size , sort);
        var pageData = userRepository.findAllByRoleName("USER",pageable);

        return ListUserResponse.<UserResponse>builder()
                .totalUser(pageData.getTotalElements())
                .currentPage(page+1)
                .size(pageData.getSize())
                .users(pageData.getContent().stream().map(userMapper::toUserResponse).toList())
                .build();
    }

    @Override
    public UserResponse getUser(String email) {
        return userMapper.toUserResponse(
                userRepository.findByEmail(email).orElseThrow(() -> new IdentityException(ErrorCode.USER_NOT_EXISTED))
        );
    }

    @Override
    public UserResponse updateUser( UserUpdatedRequest userRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName())
                                        .orElseThrow(() -> new IdentityException(ErrorCode.USER_NOT_EXISTED));
        userMapper.toUser(userRequest,user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Override
    public UserResponse updateRoleUser(String email,String roleRequest) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IdentityException(ErrorCode.USER_NOT_EXISTED));

        Role role = roleRepository.findById(roleRequest)
                .orElseThrow(() -> new IdentityException(ErrorCode.INVALID_ROLE_NOT_EXIST));

        LevelRole roleOfUser = getMaxRoleUser(user);

        if(LevelRole.ADMIN.hasHigherAuthority(roleOfUser)
                || LevelRole.ADMIN.hasHigherAuthority(LevelRole.valueOf(role.getName())))
            throw  new IdentityException(ErrorCode.UNAUTHORIZED);

        user.getRoles().add(role);

        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Override
    public Map<String,UserIdResponse> getUserId(String email) {
        List<String> listEmail = new ArrayList<>();
        listEmail.add(email);
        listEmail.add(SecurityContextHolder.getContext().getAuthentication().getName());
        return userRepository.findAllByEmailIn(listEmail).stream()
                .collect(Collectors.toMap(User::getEmail, user -> new UserIdResponse(user.getId())));
    }

    private LevelRole getMaxRoleUser(User user) {
        return user.getRoles().stream()
                .map(userRole -> LevelRole.valueOf(userRole.getName()))
                .max(Comparator.comparingInt(LevelRole::getLevel))
                .orElse(LevelRole.USER);
    }
}
