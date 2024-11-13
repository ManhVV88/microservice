package com.microservice.walletservice.dto.request;

import com.microservice.walletservice.constant.EntityValidated;
import com.microservice.walletservice.constant.Existed;
import com.microservice.walletservice.validator.CurrencyCodeConstraint;
import com.microservice.walletservice.validator.IsExistedEntityIdConstraint;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PriceItemRequest {
    @IsExistedEntityIdConstraint(
            message = "INVALID_TYPE_ITEM_NOT_EXISTED",
            isExisted = Existed.EXISTED_IS_PASSED,
            entityValidated = EntityValidated.PRICE_ITEM)
    @Size(min = 3,max = 7 , message = "INVALID_SIZE")
    @Pattern(regexp = "^[A-Z]+$",message = "INVALID_PATTERN_ITEM_TYPE")
    String walletItemType;

    Double priceForItem;
    Long quantity;

    @CurrencyCodeConstraint
    String currencyCode;
}
