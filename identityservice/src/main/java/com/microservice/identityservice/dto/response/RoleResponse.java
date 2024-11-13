package com.microservice.identityservice.dto.response;

import com.microservice.identityservice.entity.Permission;
import jakarta.persistence.ManyToMany;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class RoleResponse {
    String name;
    String description;
    LocalDateTime createDate;
    LocalDateTime updateDate;

    Set<PermissionResponse> permissions;
}
