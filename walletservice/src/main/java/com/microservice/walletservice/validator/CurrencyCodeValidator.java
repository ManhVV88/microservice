package com.microservice.walletservice.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.Currency;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class CurrencyCodeValidator implements ConstraintValidator<CurrencyCodeConstraint,String> {

    String currencyCode;

    @Override
    public void initialize(CurrencyCodeConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        currencyCode = constraintAnnotation.currencyCode();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if(s == null)
            return false;

        try {
            var curCode = Currency.getInstance(s);
            if(currencyCode.equals(curCode.toString())) {
                return true;
            }
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return false;
        }
        return false;
    }
}
