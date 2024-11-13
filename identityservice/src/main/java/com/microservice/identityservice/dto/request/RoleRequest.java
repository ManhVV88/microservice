package com.microservice.identityservice.dto.request;

import com.microservice.identityservice.constant.EntityValidated;
import com.microservice.identityservice.constant.Existed;
import com.microservice.identityservice.validator.IsExistedEntityIdConstraint;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class RoleRequest {
    @Size(min = 3,max = 7 , message = "INVALID_SIZE")
    @Pattern(regexp = "^[A-Z]+$",message = "INVALID_PATTERN_PERMISSION")
    @IsExistedEntityIdConstraint(entityValidated = EntityValidated.ROLE
            ,isExisted = Existed.NOT_EXISTED_IS_PASSED
            ,message = "INVALID_ROLE_EXISTED")
    String name;
    String description;
}
