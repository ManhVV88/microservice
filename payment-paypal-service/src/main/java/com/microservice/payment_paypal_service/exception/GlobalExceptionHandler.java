package com.microservice.payment_paypal_service.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.commonbase.service.dto.ApiResponse;
import feign.FeignException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private static final String MIN_ATTRIBUTE = "min";
    private static final String MAX_ATTRIBUTE = "max";
    private static final String FIELD_NAME = "fieldName";

    private final ExStringMessageFormater stringMessageFormater;

    public GlobalExceptionHandler(ExStringMessageFormater stringMessageFormater) {
        this.stringMessageFormater = stringMessageFormater;
    }

    @ExceptionHandler(PaypalException.class)
    ResponseEntity<ApiResponse<Object>> handleIdentityException(PaypalException e) {
        ErrorCode errorCode = e.getErrorCode();

        return ResponseEntity.status(errorCode.getStatusCode())
                .body(ApiResponse.builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build());
    }

    @ExceptionHandler(JwtException.class)
    ResponseEntity<ApiResponse<Object>> handleJwtException(JwtException e) {
        ErrorCode errorCode = ErrorCode.UNAUTHENTICATED;

        return ResponseEntity.status(errorCode.getStatusCode())
                .body(ApiResponse.builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    ResponseEntity<ApiResponse<Object>> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException ex) {
        ErrorCode errorCode = ErrorCode.INVALID_CONVERT;

        return ResponseEntity.status(errorCode.getStatusCode())
                .body(ApiResponse.builder()
                        .code(errorCode.getCode())
                        .message(stringMessageFormater.format(ex,errorCode.getMessage()))
                        .build());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    ResponseEntity<ApiResponse<Object>> handleConstraintViolationException(ConstraintViolationException ex) {
        ErrorCode errorCode = ErrorCode.INVALID_KEY;
         var constraintViolation = ex.getConstraintViolations().stream().findFirst()
                 .orElseThrow(() -> {
                     log.error("handleConstraintViolationException : {}", ex.getMessage());
                     return new PaypalException(ErrorCode.UNCATEGORIZED_EXCEPTION);
                 });
        Map<String , Object> attributes = null;
        String fieldName = "";
        try {
            errorCode = ErrorCode.valueOf(constraintViolation.getMessageTemplate());
            Path path = constraintViolation.getPropertyPath();
            attributes = constraintViolation.getConstraintDescriptor().getAttributes() ;
            fieldName = getLastFieldName(path);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
        }

        return ResponseEntity.status(errorCode.getStatusCode())
                .body(ApiResponse.builder()
                        .code(errorCode.getCode())
                        .message( Objects.nonNull(attributes)
                                ? mapAttribute(errorCode.getMessage(),attributes,fieldName)
                                : errorCode.getMessage())
                        .build());
    }


    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<ApiResponse<Object>> handlingAccessDeniedException(AccessDeniedException exception) {
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;

        return ResponseEntity.status(errorCode.getStatusCode())
                .body(ApiResponse.builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse<Object>>
        handleMethodArgumentNotValidException(MethodArgumentNotValidException exception)
    {
        var fieldError = Objects.requireNonNull(exception.getFieldError());


        ErrorCode errorCode = ErrorCode.INVALID_KEY;
        Map<String , Object> attributes = null ;
        try {
            errorCode = ErrorCode.valueOf(fieldError.getDefaultMessage());
            var constraintViolations = exception.getBindingResult().getAllErrors()
                    .getFirst().unwrap(ConstraintViolation.class);

            attributes = constraintViolations.getConstraintDescriptor().getAttributes();

        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
        }

        return ResponseEntity.badRequest().body(
                ApiResponse.builder()
                        .code(errorCode.getCode())
                        .message(
                                Objects.nonNull(attributes)
                                        ? mapAttribute(errorCode.getMessage(),attributes,fieldError.getField())
                                        : errorCode.getMessage()
                        )
                        .build()
        );
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    ResponseEntity<ApiResponse<Object>> handlingHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        ErrorCode errorCode = ErrorCode.INVALID_JSON_PARSE;

        Throwable cause = ex.getMostSpecificCause();

        return ResponseEntity.status(errorCode.getStatusCode())
                .body(ApiResponse.builder()
                        .code(errorCode.getCode())
                        .message(
                                errorCode.getMessage()+cause.getMessage()
                        )
                        .build());
    }

    @ExceptionHandler(value = FeignException.class)
    ResponseEntity<ApiResponse<Object>> handleFeignException(FeignException e) {
        String message = e.getMessage();
        String json = extractJson(message);

        int code = 1000;
        String errorMessage = "Unknown error";

        // Parse JSON để lấy code và message
        if (json != null) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                List<Map<String, Object>> errors = objectMapper.readValue(json, List.class);

                if (!errors.isEmpty()) {
                    Map<String, Object> errorDetails = errors.getFirst();
                    code = (int) errorDetails.get("code");
                    errorMessage = (String) errorDetails.get("message");
                }
            } catch (Exception ex) {
                // Handle parsing error
                errorMessage = "Failed to parse error response";
            }
        }
        return ResponseEntity.status(e.status())
                .body(ApiResponse.builder()
                        .code(code)
                        .message(errorMessage)
                        .build());
    }

    private String mapAttribute(String message, Map<String, Object> attributes,String... fieldName) {

        if (fieldName.length > 0) {
            message = message.replace("{" + FIELD_NAME + "}", Arrays.stream(fieldName).findFirst().get());
        }

        if(attributes != null) {
            String minValue = "";
            String maxValue = "";
            if(attributes.get(MIN_ATTRIBUTE) != null) {
                minValue = String.valueOf(attributes.get(MIN_ATTRIBUTE));
                maxValue = String.valueOf(attributes.get(MAX_ATTRIBUTE));
            } else if (attributes.get("value") != null) {
                minValue = String.valueOf(attributes.get("value"));
                maxValue = String.valueOf(attributes.get("value"));
            }
            message = message.replace("{" + MIN_ATTRIBUTE + "}", minValue);
            message = message.replace("{" + MAX_ATTRIBUTE + "}", maxValue);
        }

        return message;
    }

    private String getLastFieldName(Path propertyPath) {
        String lastElement = null;
        for (Path.Node node : propertyPath) {
            lastElement = node.getName();
        }
        return lastElement;
    }


    private String extractJson(String message) {
        Pattern pattern = Pattern.compile("(\\[\\{\"code\":\\d+,\"message\":\"[^\"]+\"\\}\\])");
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
}
