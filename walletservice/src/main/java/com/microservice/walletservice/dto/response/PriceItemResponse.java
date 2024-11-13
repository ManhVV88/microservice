package com.microservice.walletservice.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PriceItemResponse {
    String walletItemType;
    Double priceForItem;
    Long quantity;
    String currencyCode;
}