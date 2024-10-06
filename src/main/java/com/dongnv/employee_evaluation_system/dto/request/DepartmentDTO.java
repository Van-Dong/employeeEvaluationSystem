package com.dongnv.employee_evaluation_system.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentDTO {

    Integer id;

    @NotNull(message = "Code must not be null")
    @NotBlank(message = "Code must not be blank")
    String code;

    @NotNull(message = "Name must not be null")
    @NotBlank(message = "Name must not be blank")
    String name;
}
