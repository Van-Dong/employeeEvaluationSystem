package com.dongnv.employee_evaluation_system.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SetPasswordRequest {
    @NotNull
    @Size(min = 5, message = "Password must be at least 5 character")
    String new_password;
}
