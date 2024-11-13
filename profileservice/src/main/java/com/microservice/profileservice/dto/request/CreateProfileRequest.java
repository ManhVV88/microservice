package com.microservice.profileservice.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class CreateProfileRequest {
    String firstName;
    String lastName;
    LocalDate dateOfBirth;
    String address;
}
