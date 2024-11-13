package com.microservice.walletservice.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateRechargeResponse {
    Double amount;
    String currency;
    String status;
    String userId;
    String email;
    Double previousBalance;
    Double currentBalance;
    String paymentMethod;
}
