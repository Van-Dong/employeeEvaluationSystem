package com.dongnv.employee_evaluation_system.dto.response;

import com.dongnv.employee_evaluation_system.model.Employee;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DepartmentScore {
    Integer id;
    String code;
    String name;
    Long totalRewards;
    Long totalDisciplines;

}
