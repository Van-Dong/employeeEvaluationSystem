package com.dongnv.employee_evaluation_system.dto.request;

import com.dongnv.employee_evaluation_system.constant.UserRole;
import com.dongnv.employee_evaluation_system.validation.annotation.RoleConstraint;
import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Arrays;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SetRoleRequest {
    @RoleConstraint(value = {"ADMIN", "MANAGER", "CUSTOMER"})
    String role;
}
