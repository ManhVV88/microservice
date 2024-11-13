package com.microservice.paymentservice.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WalletUserResponse {
    String userId;
    BalanceResponse balanceResponse;
    Set<DetailWalletUserResponse> listDetail;
}
