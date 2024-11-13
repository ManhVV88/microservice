package com.microservice.fileservice.exception;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FileException extends RuntimeException {
    ErrorCode errorCode;
}
