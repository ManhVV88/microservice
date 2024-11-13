package com.microservice.gateway.configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.commonbase.service.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Component
@Slf4j
public class CustomRateLimiterFilter extends AbstractGatewayFilterFactory<CustomRateLimiterFilter.Config> {
    private final RedisRateLimiter redisRateLimiter;
    private final ObjectMapper objectMapper;

    public CustomRateLimiterFilter(RedisRateLimiter redisRateLimiter, ObjectMapper objectMapper) {
        super(Config.class);
        this.redisRateLimiter = redisRateLimiter;
        this.objectMapper = objectMapper;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
            if (route == null) {
                log.error("Route is null. Cannot apply rate limiting.");
                return chain.filter(exchange);
            }

            String key = Objects.requireNonNull(exchange.getRequest().getRemoteAddress()).getAddress().getHostAddress();
            return redisRateLimiter.isAllowed(route.getId(), key)
                    .flatMap(response -> {
                        if (!response.isAllowed()) {
                            exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
                            exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

                            ApiResponse<?> apiResponse = ApiResponse.builder()
                                    .code(1429)
                                    .message("Too many requests, please try again later.")
                                    .build();
                            String responseBody;
                            try {
                                responseBody = objectMapper.writeValueAsString(apiResponse);
                            } catch (JsonProcessingException e) {
                                log.error("Failed to serialize API response: {}", e.getMessage());
                                return Mono.error(new RuntimeException(e));
                            }
                            DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(responseBody.getBytes());
                            return exchange.getResponse().writeWith(Mono.just(buffer));
                        }
                        return chain.filter(exchange);
                    });
        };
    }

    public static class Config {
        // Thêm các thuộc tính tùy chỉnh nếu cần
    }
}
