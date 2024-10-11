package com.dongnv.employee_evaluation_system.dto.response;

import com.dongnv.employee_evaluation_system.model.Employee;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmployeeScore {
    Long id;
    Long totalRewards;
    Long totalDisciplines;
    Employee employee;

    public EmployeeScore(long id, long totalRewards, long totalDisciplines) {
        this.id = id;
        this.totalRewards = totalRewards;
        this.totalDisciplines = totalDisciplines;
    }
}
