package com.dongnv.employee_evaluation_system.service;

import com.dongnv.employee_evaluation_system.model.Department;

import java.util.List;

public interface DepartmentService {
    List<Department> getAll();
    Department getById(Long id);
    Department save(Department department);
    void deleteById(Long id);
    boolean existsByName(String name);
    boolean existsByCode(String code);
}
