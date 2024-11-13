package com.microservice.payment_paypal_service.controller;

import com.microservice.commonbase.service.dto.ApiResponse;
import com.microservice.payment_paypal_service.dto.request.PaymentRequest;
import com.microservice.payment_paypal_service.dto.response.PaymentPaypalResponse;
import com.microservice.payment_paypal_service.dto.request.PaymentSuccessRequest;
import com.microservice.payment_paypal_service.dto.response.PaymentSucessResponse;
import com.microservice.payment_paypal_service.service.PaypalService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class PaypalController {
    PaypalService paypalService;
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<PaymentPaypalResponse>> createPayment(@RequestBody PaymentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<PaymentPaypalResponse>builder()
                        .result(paypalService.createPayment(request))
                        .build()
        );
    }

    @GetMapping("/success")
    public ApiResponse<PaymentSucessResponse> successPayment(
            @RequestParam String rechargeId,
            @RequestParam String token) {
        return ApiResponse.<PaymentSucessResponse>builder()
                .result(paypalService.successPayment(rechargeId, token))
                .build();
    }

}
