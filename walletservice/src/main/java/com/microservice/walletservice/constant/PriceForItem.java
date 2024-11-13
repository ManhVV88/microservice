package com.microservice.walletservice.constant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal=true)
public enum PriceForItem {
    STAR("STAR", 1000L,1D,"USD"),
    DIAMOND("DIAMOND", 1000L,2D,"USD"),
    ;

    String walletItemType;
    Long quantity;
    Double priceForItem;
    String currencyCode;
}
