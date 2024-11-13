package com.microservice.walletservice.validator;

import com.microservice.walletservice.constant.EntityValidated;
import com.microservice.walletservice.constant.Existed;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {IsExistedEntityIdValidator.class})
public @interface IsExistedEntityIdConstraint {
    String message() default "ID_EXISTED";

    Existed isExisted() default Existed.EXISTED_IS_PASSED;

    EntityValidated entityValidated() default  EntityValidated.PRICE_ITEM;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}