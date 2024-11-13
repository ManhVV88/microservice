package com.microservice.profileservice.repository.fallbackFactory;

import com.microservice.commonbase.service.dto.ApiResponse;
import com.microservice.profileservice.repository.httpClient.IdentityClient;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class IdentityClientFallBackFactory implements FallbackFactory<IdentityClient> {
    @Override
    public IdentityClient create(Throwable cause) {
        return new IdentityClient() {

            @Override
            public ApiResponse<Boolean> isExistEmail(String email) {
                if (cause instanceof FeignException feignException) {
                    int status = feignException.status();  // Lấy mã lỗi HTTP
                    String message = feignException.getMessage();
                    log.info("Feign exception: {}", message);
                    log.info("Feign status: {}", status);
                }

                // Trả về giá trị fallback mặc định
                return ApiResponse.<Boolean>builder()
                        .result(false)
                        .build();
            }
        };
    }
}
