package com.dongnv.employee_evaluation_system.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
    Long id;

    @NotNull
    @Size(min = 4, message = "Username must be at least 4 character")
    String username;

    @NotNull
    @Size(min = 5, message = "Password must be at least 5 character")
    String password;

    //    Boolean isActive;

    LocalDate createdDate;
}
