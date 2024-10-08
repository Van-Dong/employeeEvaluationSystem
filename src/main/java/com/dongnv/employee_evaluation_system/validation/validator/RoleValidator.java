package com.dongnv.employee_evaluation_system.validation.validator;

import com.dongnv.employee_evaluation_system.validation.annotation.RoleConstraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RoleValidator implements ConstraintValidator<RoleConstraint, String> {
    private String[] roles;

    @Override
    public void initialize(RoleConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        this.roles = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        for (String role : roles) {
            if (role.equals(s)) return true;
        }
        return false;
    }
}
