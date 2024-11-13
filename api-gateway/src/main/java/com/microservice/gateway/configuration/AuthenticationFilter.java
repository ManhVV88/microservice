package com.microservice.gateway.configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.commonbase.service.dto.ApiResponse;
import com.microservice.gateway.service.IdentityService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PACKAGE,makeFinal = true)
@Slf4j
public class AuthenticationFilter implements GlobalFilter, Ordered {

    IdentityService identityService;
    ObjectMapper objectMapper;


    @NonFinal
    private String[] PuclicEnpoint = {
            "/identity/auth/.*",
            "/identity/users/registration"
    };

    @Value("${app.api-prefix}")
    @NonFinal
    private String apiPrefix;



    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        if(isPublicEnpoint(exchange.getRequest()))
            return chain.filter(exchange);

        List<String> authHeader = exchange.getRequest().getHeaders().get("Authorization");
        if(CollectionUtils.isEmpty(authHeader))
            return authenticated(exchange.getResponse());

        String token = authHeader.getFirst().replace("Bearer ", "");

        return identityService.validateToken(token).flatMap(apiResponse -> {
            if (apiResponse.getResult().isValidToken())
                return chain.filter(exchange);
            else
                return authenticated(exchange.getResponse());
        }).onErrorResume(throwable -> authenticated(exchange.getResponse()));
    }

    @Override
    public int getOrder() {
        return -1;
    }



    private boolean isPublicEnpoint(ServerHttpRequest httpRequest) {
        return Arrays.stream(PuclicEnpoint).anyMatch(s -> httpRequest.getURI().getPath().matches(apiPrefix+s));
    }

    Mono<Void> authenticated(ServerHttpResponse response) {

            ApiResponse<?> apiResponse = ApiResponse.builder()
                    .code(1401)
                    .message("Unauthenticated")
                    .build();

            String body = null;
            try {
                body = objectMapper.writeValueAsString(apiResponse);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
            return response.writeWith(
                    Mono.just(response.bufferFactory().wrap(body.getBytes())));

    }
}
