package com.microservice.identityservice.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class UserResponse {
    String email;

    @JsonIgnore
    String password;
    LocalDate dateOfBirth;
    String name;
    String address;

    Set<RoleResponse> roles;
}
