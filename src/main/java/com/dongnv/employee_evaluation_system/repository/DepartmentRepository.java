package com.dongnv.employee_evaluation_system.repository;

import com.dongnv.employee_evaluation_system.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    boolean existsByName(String name);
    boolean existsByCode(String code);
}
