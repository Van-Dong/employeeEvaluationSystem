package com.dongnv.employee_evaluation_system.dto.request;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import jakarta.validation.constraints.*;

import org.springframework.web.multipart.MultipartFile;

import com.dongnv.employee_evaluation_system.validation.annotation.DobConstraint;
import com.dongnv.employee_evaluation_system.validation.annotation.FileConstraint;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmployeeDTO {
    Long id;

    @NotNull
    @NotBlank
    String fullName;

    @NotNull
    Boolean isMale;

    @NotNull
    @DobConstraint(min = 18)
    LocalDate dob;

    @NotNull
    @Min(value = 1000, message = "Salary must be at least 1000")
    Double salary;

    @NotNull
    @Min(value = 1, message = "Level must be between in [1, 10]")
    @Max(value = 10, message = "Level must be between in [1, 10]")
    Byte level;

    @Email(message = "Invalid email")
    String email;

    String phone;

    @Size(max = 255, message = "Note must be max 255 characters")
    String note;

    Integer departmentId;
    String imageUrl;

    @FileConstraint(message = "Type of image not support")
    MultipartFile imageFile;

    public String getFormattedDob() {
        return dob == null ? "" : dob.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
