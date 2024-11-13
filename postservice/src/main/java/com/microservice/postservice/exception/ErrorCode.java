package com.microservice.postservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Uncategorized error", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1006, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, "You do not have permission", HttpStatus.FORBIDDEN),
    INVALID_CONVERT(1018, "{fieldName}={value} can't convert to {typeRequired}", HttpStatus.BAD_REQUEST),
    CANNOT_SEND_EMAIL(1008, "Cannot send email", HttpStatus.BAD_REQUEST),
    INVALID_JSON_PARSE(1016, "can't parse to json because , ", HttpStatus.BAD_REQUEST),
    INVALID_PAGE_MIN(1017, "{fieldName} must be >= {min}", HttpStatus.BAD_REQUEST),
    INVALID_NOT_FOUND(1017, "Post not found", HttpStatus.NOT_FOUND),
    IMAGES_URL_NOT_NULL(1017, "Image url not null", HttpStatus.BAD_REQUEST),
    ;
    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}
