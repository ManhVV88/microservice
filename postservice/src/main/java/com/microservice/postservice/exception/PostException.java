package com.microservice.postservice.exception;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostException extends RuntimeException {
    ErrorCode errorCode;
}
