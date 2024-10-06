package com.dongnv.employee_evaluation_system.validation.validator;

import com.dongnv.employee_evaluation_system.validation.annotation.FileConstraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

public class FileValidator implements ConstraintValidator<FileConstraint, MultipartFile> {
    private static final Set<String> ALLOWED_IMAGE_TYPE = Set.of(
            "image/jpeg",
            "image/png",
            "image/gif",
            "image/bmp",
            "image/webp"
    );

    @Override
    public void initialize(FileConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext constraintValidatorContext) {
        if (multipartFile == null || multipartFile.isEmpty()) return true;

        return ALLOWED_IMAGE_TYPE.contains(multipartFile.getContentType());
    }
}
