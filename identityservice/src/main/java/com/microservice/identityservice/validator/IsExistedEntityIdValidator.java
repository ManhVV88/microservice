package com.microservice.identityservice.validator;

import com.microservice.identityservice.constant.EntityValidated;
import com.microservice.identityservice.constant.Existed;
import com.microservice.identityservice.validator.strategy.IsExistedStrategy;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IsExistedEntityIdValidator implements ConstraintValidator<IsExistedEntityIdConstraint,String> {

    Existed existed;
    EntityValidated entityValidated;

    final IsExistedStrategy isExistedStrategy;

    @Override
    public void initialize(IsExistedEntityIdConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        existed = constraintAnnotation.isExisted();
        entityValidated = constraintAnnotation.entityValidated();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        System.out.println("IsExistedEntityIdValidator is being called with input: " + s);
        return isExistedStrategy.isExist(entityValidated,existed,s);
    }
}
