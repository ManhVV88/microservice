package com.microservice.gateway.repository;

import com.microservice.commonbase.service.dto.ApiResponse;
import com.microservice.gateway.dto.request.ValidateTokenRequest;
import com.microservice.gateway.dto.response.ValidateTokenResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;
import reactor.core.publisher.Mono;

public interface IdentityClient {
    @PostExchange(url = "/auth/validation", contentType = MediaType.APPLICATION_JSON_VALUE)
    Mono<ApiResponse<ValidateTokenResponse>> login(@RequestBody ValidateTokenRequest validateTokenRequest);
}
