package com.microservice.walletservice.validator;

import com.microservice.walletservice.constant.EntityValidated;
import com.microservice.walletservice.constant.Existed;
import com.microservice.walletservice.validator.strategy.IsExistedStrategy;
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
        return isExistedStrategy.isExist(entityValidated,existed,s);
    }
}
