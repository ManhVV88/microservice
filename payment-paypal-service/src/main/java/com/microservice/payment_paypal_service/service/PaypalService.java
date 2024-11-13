package com.microservice.payment_paypal_service.service;

import com.microservice.payment_paypal_service.dto.request.PaymentRequest;
import com.microservice.payment_paypal_service.dto.response.PaymentPaypalResponse;
import com.microservice.payment_paypal_service.dto.request.PaymentSuccessRequest;
import com.microservice.payment_paypal_service.dto.response.PaymentSucessResponse;

public interface PaypalService {
    PaymentPaypalResponse createPayment(PaymentRequest request);
    PaymentSucessResponse successPayment(String rechargeId, String token);
}
