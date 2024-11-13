package com.microservice.paymentservice.controller;

import com.microservice.commonbase.service.dto.ApiResponse;
import com.microservice.paymentservice.dto.request.PaymentSuccessRequest;
import com.microservice.paymentservice.dto.response.WalletResponse;
import com.microservice.paymentservice.service.PaymentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal=true)
@RequiredArgsConstructor
public class PaymentController {

    PaymentService paymentService;
    @PostMapping("/payment-success")
    public ApiResponse<WalletResponse> paymentSuccess(
            @RequestBody PaymentSuccessRequest paymentRequest) {
        return ApiResponse.<WalletResponse>builder()
                        .result(paymentService.paymentSuccess(paymentRequest))
                        .build();
    }
}
