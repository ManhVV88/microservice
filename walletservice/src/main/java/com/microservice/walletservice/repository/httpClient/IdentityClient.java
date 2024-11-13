package com.microservice.walletservice.repository.httpClient;

import com.microservice.commonbase.service.dto.ApiResponse;
import com.microservice.walletservice.configuration.AuthenticationRequestInterceptor;
import com.microservice.walletservice.dto.response.UserIdResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.validation.constraints.Null;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient(name = "identity-service" , url = "${app.service.identity}"
        ,configuration = {AuthenticationRequestInterceptor.class})
public interface IdentityClient {
    Logger log = LoggerFactory.getLogger(IdentityClient.class);

    @GetMapping(value = "/users/get_id/{email}",produces = MediaType.APPLICATION_JSON_VALUE)
    @Retry(name = "restApi")
    @CircuitBreaker(name="restCircuitBreaker",fallbackMethod = "getUserIdFallback" )
    ApiResponse<Map<String,UserIdResponse>> getUserId(@PathVariable("email") String email);



    default ApiResponse<Map<String,UserIdResponse>> getUserIdFallback(String email,Throwable ex) {
        log.error("getUserIdFallback {} : {}",email,ex.getMessage());
        return ApiResponse.<Map<String,UserIdResponse>>builder()
                .result(null)
                .build();
    }
}
