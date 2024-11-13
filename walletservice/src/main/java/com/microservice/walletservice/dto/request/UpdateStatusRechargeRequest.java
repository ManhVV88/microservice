package com.microservice.walletservice.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateStatusRechargeRequest {
    String status;
    String rechargeId;
    String gatewayTransactionId;
}
