package com.dongnv.employee_evaluation_system.repository;

import com.dongnv.employee_evaluation_system.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query("SELECT COUNT(e) FROM Employee e WHERE e.department.id = :departmentId")
    Integer countByDepartmentId(@Param("departmentId") Integer departmentId);

    @Query("SELECT e FROM Employee e WHERE e.department.id = :departmentId")
    Page<Employee> findByDepartmentId(@Param("departmentId") Integer departmentId, Pageable pageable);

    List<Employee> findAllByIdIn(List<Long> ids);

    Page<Employee> findAllByFullNameLike(String fullName, Pageable pageable);
}
