package com.dongnv.employee_evaluation_system.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DepartmentDTO {

    Integer id;

    @NotNull(message = "Code must not be null")
    @NotBlank(message = "Code must not be blank")
    String code;

    @NotNull(message = "Name must not be null")
    @NotBlank(message = "Name must not be blank")
    String name;
}
