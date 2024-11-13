package com.microservice.gateway.service.impl;

import com.microservice.commonbase.service.dto.ApiResponse;
import com.microservice.gateway.dto.request.ValidateTokenRequest;
import com.microservice.gateway.dto.response.ValidateTokenResponse;
import com.microservice.gateway.repository.IdentityClient;
import com.microservice.gateway.service.IdentityService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class IdentityServiceImpl implements IdentityService {

    IdentityClient identityClient;

    @Override
    public Mono<ApiResponse<ValidateTokenResponse>> validateToken(String token) {
        return identityClient.login(ValidateTokenRequest.builder().token(token).build());
    }
}
