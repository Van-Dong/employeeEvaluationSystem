package com.dongnv.employee_evaluation_system.service;

import com.dongnv.employee_evaluation_system.dto.response.DepartmentScore;
import com.dongnv.employee_evaluation_system.dto.response.EmployeeScore;
import com.dongnv.employee_evaluation_system.model.Employee;
import com.dongnv.employee_evaluation_system.repository.EmployeeRepository;
import com.dongnv.employee_evaluation_system.repository.EvaluationRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class RankingService {
    EvaluationRepository evaluationRepository;
    EmployeeRepository employeeRepository;

    public Page<EmployeeScore> getEmployeeScore(String searchName, Integer page) {
        // Get score employee order by score
        Page<EmployeeScore> employeeScorePage = evaluationRepository.personalAchievementsByEmployeeName("%" + searchName + "%", PageRequest.of(page, 10));

        // Get employees base on IDs
        List<Long> ids = employeeScorePage.stream().map(EmployeeScore::getId).toList();
        List<Employee> employees = employeeRepository.findAllByIdIn(ids);
        Map<Long, Employee> employeeMap = employees.stream().collect(Collectors.toMap(Employee::getId, e -> e));

        // Set employee for EmployeeScore
        employeeScorePage.forEach(employeeScore -> employeeScore.setEmployee(
                employeeMap.get(employeeScore.getId()))
        );
        return employeeScorePage;
    }

    public Page<DepartmentScore> getDepartmentScore(String searchName, Integer page) {
        // Get score department order by score
        return evaluationRepository.departmentAchievementsByNameLike("%" + searchName + "%", PageRequest.of(page, 10));
    }
}
