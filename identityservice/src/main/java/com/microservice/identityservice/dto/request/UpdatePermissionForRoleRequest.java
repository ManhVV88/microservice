package com.microservice.identityservice.dto.request;

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
public class UpdatePermissionForRoleRequest {
    @Pattern(regexp = "^(ADD|DEL)$", message = "INVALID_PATTERN_TYPE_UPDATE")
    String updateType;

    @Size(min = 3,max = 5 , message = "INVALID_SIZE")
    @Pattern(regexp = "^[A-Z]+$",message = "INVALID_PATTERN_PERMISSION")
    String permissionName;
}
