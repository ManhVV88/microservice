package com.microservice.fileservice.exception;

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
    INVALID_JSON_PARSE(1016, "can't parse to json because , ", HttpStatus.BAD_REQUEST),
    INVALID_IMAGE_FILE_TYPE(1016, "{fieldName} just accept {allowedTypes}", HttpStatus.BAD_REQUEST)
    ;
    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}
