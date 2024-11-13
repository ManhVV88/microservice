package com.microservice.paymentservice.repository.httpClient;

import com.microservice.commonbase.service.dto.ApiResponse;
import com.microservice.paymentservice.configuration.AuthenticationRequestInterceptor;
import com.microservice.paymentservice.dto.request.UpdateStatusRechargeRequest;
import com.microservice.paymentservice.dto.request.WalletUpdateRequest;
import com.microservice.paymentservice.dto.response.WalletUserResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "wallet-service" , url = "${app.service.wallet}"
        ,configuration = {AuthenticationRequestInterceptor.class})
public interface WalletClient {
    @PutMapping(value = "/recharge_point", produces = MediaType.APPLICATION_JSON_VALUE)
    @Retry(name = "restApi")
    @CircuitBreaker(name="restCircuitBreaker",fallbackMethod = "updateStatusRechargeFallback" )
    ApiResponse<String> updateStatusRecharge(@RequestBody UpdateStatusRechargeRequest request);

    @PostMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    @Retry(name = "restApi")
    @CircuitBreaker(name="restCircuitBreaker",fallbackMethod = "updateWalletUserFallback" )
    ApiResponse<WalletUserResponse> updateWalletUser(@RequestBody WalletUpdateRequest request);

    default ApiResponse<String> updateStatusRechargeFallback(UpdateStatusRechargeRequest request, Throwable throwable) {
        return ApiResponse.<String>builder()
                .result("null")
                .build();
    }

    default ApiResponse<WalletUserResponse> updateWalletUserFallback(WalletUpdateRequest request, Throwable throwable) {
        return ApiResponse.<WalletUserResponse>builder()
                .result(null)
                .build();
    }
}
