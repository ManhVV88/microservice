package com.microservice.payment_paypal_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentPaypalResponse {
    String status;
    String paymentId;
    String paypalUrl;
}
