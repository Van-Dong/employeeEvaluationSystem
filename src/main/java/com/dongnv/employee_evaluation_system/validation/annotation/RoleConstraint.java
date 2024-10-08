package com.dongnv.employee_evaluation_system.validation.annotation;

import com.dongnv.employee_evaluation_system.validation.validator.RoleValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = RoleValidator.class)
public @interface RoleConstraint {
    String message() default "Invalid role";
    String[] value() default {};
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
