package com.dongnv.employee_evaluation_system.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.Size;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EvaluationDTO {
    Long id;

    @Builder.Default
    Boolean isCommended = false;

    @Size(max = 255, message = "Reason must be max 255 characters")
    String reason;

    LocalDate evaluationDate;
}
