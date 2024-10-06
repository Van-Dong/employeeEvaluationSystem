package com.dongnv.employee_evaluation_system.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    Long id;

    @Size(min = 4, message = "Username must be at least 4 character")
    String username;

    @Size(min = 5, message = "Password must be at least 5 character")
    String password;

    Boolean isActive;
}
