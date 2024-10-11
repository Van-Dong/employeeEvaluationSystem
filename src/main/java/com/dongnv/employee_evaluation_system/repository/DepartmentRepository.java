package com.dongnv.employee_evaluation_system.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dongnv.employee_evaluation_system.model.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer> {
    Page<Department> findAllByNameLike(String name, Pageable pageable);
}
