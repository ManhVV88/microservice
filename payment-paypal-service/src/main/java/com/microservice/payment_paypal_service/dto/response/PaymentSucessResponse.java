package com.microservice.payment_paypal_service.dto.response;

import com.microservice.payment_paypal_service.dto.request.PaymentSuccessRequest;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentSucessResponse {
    WalletResponse walletResponse;
    PaymentSuccessRequest paymentSuccessRequest;
}
