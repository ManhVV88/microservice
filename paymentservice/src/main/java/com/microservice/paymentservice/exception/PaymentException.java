package com.microservice.paymentservice.exception;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentException extends RuntimeException {
    ErrorCode errorCode;
}
