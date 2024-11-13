package com.microservice.payment_paypal_service.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentSuccessRequest {
    String rechargeId;
    BigDecimal amount;
    BigDecimal paymentFee;
    String gatewayTransactionId;
    String paymentMethod;
    String paymentStatus;
    String itemType;
    Long quantity;
    String failureMessage;

}
