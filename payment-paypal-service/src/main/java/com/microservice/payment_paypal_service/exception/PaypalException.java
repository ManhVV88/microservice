package com.microservice.payment_paypal_service.exception;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaypalException extends RuntimeException {
    ErrorCode errorCode;
}
