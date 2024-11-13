package com.microservice.walletservice.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GetRechargeIdResponse {
    String rechargeHistoryId;
    Double amount;
    Long quantity;
    String walletItemType;
}
