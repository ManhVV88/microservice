package com.microservice.gateway.service;

import com.microservice.commonbase.service.dto.ApiResponse;
import com.microservice.gateway.dto.response.ValidateTokenResponse;
import reactor.core.publisher.Mono;

public interface IdentityService {
    Mono<ApiResponse<ValidateTokenResponse>> validateToken(String token);
}
