package com.microservice.profileservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Uncategorized error", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1002, "User existed", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1004, "password does not matching", HttpStatus.BAD_REQUEST),
    INVALID_MAX(1005, "{fieldName} must be >= {max} characters", HttpStatus.BAD_REQUEST),
    INVALID_MIN(1006, "{fieldName} must be <= {min} characters", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1007, "User not existed", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1008, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1009, "You do not have permission", HttpStatus.FORBIDDEN),
    INVALID_DOB(1010, "Your age must be at least {min}", HttpStatus.BAD_REQUEST),
    INVALID_PAGEGIN(1011, "Page or size must be number", HttpStatus.BAD_REQUEST),
    INVALID_SIZE(1012, "Size of {fieldName} must be between {min} and {max}", HttpStatus.BAD_REQUEST),
    INVALID_BLANK(1013, "{fieldName} can't blank", HttpStatus.BAD_REQUEST),
    INVALID_EMAIL_FORMAT(1014, "Invalid email format", HttpStatus.BAD_REQUEST),
    INVALID_EMAIL_EXISTED(1015, "email is existed", HttpStatus.BAD_REQUEST),
    INVALID_EMAIL_NOT_EXISTED(1015, "email is existed", HttpStatus.BAD_REQUEST),
    INVALID_JSON_PARSE(1016, "can't parse to json because , ", HttpStatus.BAD_REQUEST),
    INVALID_PAGE_MIN(1017, "{fieldName} must be <= {min}", HttpStatus.BAD_REQUEST),
    INVALID_CONVERT(1018, "{fieldName}={value} can't convert to {typeRequired}", HttpStatus.BAD_REQUEST),
    INVALID_ROLE_NOT_EXIST(1019, "Role is not existed", HttpStatus.BAD_REQUEST),
    INVALID_PERMISSION_EXISTED(1020, "Permission is existed", HttpStatus.BAD_REQUEST),
    INVALID_ROLE_EXISTED(1021, "Role is existed", HttpStatus.BAD_REQUEST),
    INVALID_PERMISSION_NOT_EXIST(1020, "Permission is not exist", HttpStatus.BAD_REQUEST),
    ID_EXISTED(1022, "Id is existed", HttpStatus.BAD_REQUEST),
    INVALID_PATTERN_PERMISSION(1023, "{fieldName} just contain characters A-Z", HttpStatus.BAD_REQUEST),
    INVALID_PATTERN_TYPE_UPDATE(1023, "{fieldName} must be ADD or DEL", HttpStatus.BAD_REQUEST),
    ;
    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}
