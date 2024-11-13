package com.microservice.paymentservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    //system message
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Uncategorized error", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1006, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, "You do not have permission", HttpStatus.FORBIDDEN),

    //constraint validation exception message
    INVALID_JSON_PARSE(1016, "can't parse to json because , ", HttpStatus.BAD_REQUEST),
    INVALID_TYPE_ITEM(1017, "{fieldName} is existed", HttpStatus.BAD_REQUEST),
    INVALID_TYPE_ITEM_NOT_EXISTED(1017, "{fieldName} is not existed", HttpStatus.BAD_REQUEST),
    INVALID_CONVERT(1018, "{fieldName}={value} can't convert to {typeRequired}", HttpStatus.BAD_REQUEST),
    INVALID_PATTERN_ITEM_TYPE(1019, "{fieldName} just contain characters A-Z", HttpStatus.BAD_REQUEST),
    INVALID_SIZE(1012, "Size of {fieldName} must be between {min} and {max}", HttpStatus.BAD_REQUEST),
    INVALID_CURRENCY_CODE(1020, "{fieldName} is invalid , code must be 'USD'", HttpStatus.BAD_REQUEST),

    //wallet exception message
    INVALID_TYPE_ITEM_NOT_FOUND(1020, "Item not found", HttpStatus.BAD_REQUEST),
    INVALID_NOT_FOUND(1020, "Not found", HttpStatus.BAD_REQUEST),
    INVALID_FALLBACK(1020, "Can't Retry", HttpStatus.BAD_REQUEST),
    ;
    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}
