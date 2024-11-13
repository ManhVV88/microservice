package com.microservice.profileservice.repository.httpClient;

import com.microservice.commonbase.service.dto.ApiResponse;
import com.microservice.profileservice.configuration.AuthenticationRequestInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "identity-service" , url = "${app.service.identity}"
        ,configuration = {AuthenticationRequestInterceptor.class})
public interface IdentityClient {
    @GetMapping(value = "/users/valid-email/{email}",produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<Boolean> isExistEmail(@PathVariable String email);
}
