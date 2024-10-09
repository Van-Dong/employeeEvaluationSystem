package com.dongnv.employee_evaluation_system.dto.request;

import com.dongnv.employee_evaluation_system.validation.annotation.DobConstraint;
import com.dongnv.employee_evaluation_system.validation.annotation.FileConstraint;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {
    Long id;

    @NotNull
    @NotBlank
    String fullName;

    @NotNull
    Boolean isMale;

    @DobConstraint(min = 18)
    LocalDate dob;
    Double salary;

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
}
