package com.microservice.walletservice.repository.httpClient;

import com.microservice.commonbase.service.dto.ApiResponse;
import com.microservice.walletservice.configuration.AuthenticationRequestInterceptor;
import com.microservice.walletservice.dto.response.PostResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "post-service" , url = "${app.service.post}"
        ,configuration = {AuthenticationRequestInterceptor.class})
public interface PostClient {
    Logger log = LoggerFactory.getLogger(PostClient.class);

    @GetMapping(value = "/{postId}",produces = MediaType.APPLICATION_JSON_VALUE)
    @Retry(name = "restApi")
    @CircuitBreaker(name="restCircuitBreaker",fallbackMethod = "getPostFallback")
    ApiResponse<PostResponse> getPost(@PathVariable("postId") String postId);

    default ApiResponse<PostResponse> getPostFallback(String postId,Throwable ex) {
        log.error("getPostFallback {} , {}",postId,ex.getMessage());
        return ApiResponse.<PostResponse>builder()
                .result(null)
                .build();
    }
}
