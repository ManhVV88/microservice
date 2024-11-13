package com.microservice.profileservice.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ProfileResponse {

    String firstName;
    String lastName;
    LocalDate dateOfBirth;
    String address;
    LocalDateTime createDate;
    LocalDateTime updateDate;
}
