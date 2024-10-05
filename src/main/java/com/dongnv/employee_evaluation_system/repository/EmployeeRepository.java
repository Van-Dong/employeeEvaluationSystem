package com.dongnv.employee_evaluation_system.repository;

import com.dongnv.employee_evaluation_system.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query("SELECT COUNT(e) FROM Employee e WHERE e.department.id = :departmentId")
    Integer countByDepartmentId(@Param("departmentId") Integer departmentId);
}
