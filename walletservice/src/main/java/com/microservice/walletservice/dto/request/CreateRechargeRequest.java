package com.microservice.walletservice.dto.request;

import com.microservice.walletservice.constant.EntityValidated;
import com.microservice.walletservice.constant.Existed;
import com.microservice.walletservice.constant.PackageQuantity;
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
public class CreateRechargeRequest {

    PackageQuantity packageQuantity;

    @IsExistedEntityIdConstraint(
            message = "INVALID_TYPE_ITEM",
            isExisted = Existed.NOT_EXISTED_IS_PASSED,
            entityValidated = EntityValidated.PRICE_ITEM)
    @Size(min = 3,max = 7 , message = "INVALID_SIZE")
    @Pattern(regexp = "^[A-Z]+$",message = "INVALID_PATTERN_ITEM_TYPE")
    String typeItem;
}
