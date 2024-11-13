package com.microservice.identityservice.exception;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IdentityException extends RuntimeException {
    ErrorCode errorCode;
}
