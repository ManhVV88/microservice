package com.microservice.identityservice.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.util.Objects;

public class IsDateFormatValidator implements ConstraintValidator<IsDateFormatConstraint, LocalDate> {

    private String date;

    @Override
    public void initialize(IsDateFormatConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        date = constraintAnnotation.dateFormat();
    }

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
        if (Objects.isNull(localDate)) return true;


        return false;
    }
}
