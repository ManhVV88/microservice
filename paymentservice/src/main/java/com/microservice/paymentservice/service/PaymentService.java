package com.microservice.paymentservice.service;

import com.microservice.paymentservice.dto.request.PaymentSuccessRequest;
import com.microservice.paymentservice.dto.response.WalletResponse;

public interface PaymentService {
    WalletResponse paymentSuccess(PaymentSuccessRequest paymentSuccessRequest);
}
