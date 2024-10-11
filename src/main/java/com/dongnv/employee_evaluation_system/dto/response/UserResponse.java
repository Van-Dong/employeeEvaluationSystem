package com.dongnv.employee_evaluation_system.dto.response;

import java.time.LocalDate;

import com.dongnv.employee_evaluation_system.constant.UserRole;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    Long id;
    String username;
    Boolean isActivate;
    UserRole role;
    LocalDate createdDate;
}
