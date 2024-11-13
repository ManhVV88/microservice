package com.microservice.fileservice.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {FileTypeValidator.class})
public @interface ValidateFileType {
    String message() default "INVALID_IMAGE_FILE_TYPE";

    String[] allowedTypes();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
