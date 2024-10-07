package com.dongnv.employee_evaluation_system.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SetPasswordRequest {
    @NotNull
    @Size(min = 5, message = "Password must be at least 5 character")
    String new_password;
}
