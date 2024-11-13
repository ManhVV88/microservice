package com.microservice.payment_paypal_service.repository.httpClient;

import com.microservice.commonbase.service.dto.ApiResponse;
import com.microservice.payment_paypal_service.configuration.AuthenticationRequestInterceptor;
import com.microservice.payment_paypal_service.dto.response.GetRechargeIdResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "wallet-service" , url = "${app.service.wallet}"
        ,configuration = {AuthenticationRequestInterceptor.class})
public interface WalletClient {
    @GetMapping(value = "/recharge_point/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<GetRechargeIdResponse> getRechargeId(@PathVariable String id);
}
