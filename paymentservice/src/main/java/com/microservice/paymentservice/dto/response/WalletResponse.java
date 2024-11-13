package com.microservice.paymentservice.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WalletResponse {
    String email;
    String ItemType;
    Long priviousQuantity;
    Long currentQuantity;
}
