package com.dongnv.employee_evaluation_system.dto.response;

import com.dongnv.employee_evaluation_system.constant.UserRole;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    Long id;
    String username;
    Boolean isActivate;
    UserRole role;
    LocalDate createdDate;
}
