package com.microservice.payment_paypal_service.repository.httpClient;

import com.microservice.commonbase.service.dto.ApiResponse;
import com.microservice.payment_paypal_service.configuration.AuthenticationRequestInterceptor;
import com.microservice.payment_paypal_service.dto.request.PaymentSuccessRequest;
import com.microservice.payment_paypal_service.dto.response.WalletResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "payment-service" , url = "${app.service.payment}"
        ,configuration = {AuthenticationRequestInterceptor.class})
public interface PaymentClient {
    @PostMapping(value = "/payment-success" ,produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<WalletResponse> paymentSuccess(@RequestBody PaymentSuccessRequest paymentSuccessRequest);
}
