package com.microservice.walletservice.constant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal=true)
public enum PackageQuantity {
    PACKAGE_1K(1000L),PACKAGE_2K(2000L),PACKAGE_3K(3000L);

    long quantity;
}
