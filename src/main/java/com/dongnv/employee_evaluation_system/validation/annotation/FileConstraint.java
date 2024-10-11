package com.dongnv.employee_evaluation_system.validation.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import com.dongnv.employee_evaluation_system.validation.validator.FileValidator;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FileValidator.class)
public @interface FileConstraint {
    String message() default "Invalid Image File";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
