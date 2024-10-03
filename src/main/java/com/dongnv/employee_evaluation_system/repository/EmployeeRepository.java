package com.dongnv.employee_evaluation_system.repository;

import com.dongnv.employee_evaluation_system.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
