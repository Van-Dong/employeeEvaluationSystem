package com.dongnv.employee_evaluation_system.dto.request;

import com.dongnv.employee_evaluation_system.validation.annotation.RoleConstraint;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SetRoleRequest {
    @RoleConstraint(value = {"ADMIN", "MANAGER", "CUSTOMER"})
    String role;
}
