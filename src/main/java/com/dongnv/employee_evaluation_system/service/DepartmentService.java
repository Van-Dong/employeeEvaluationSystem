package com.dongnv.employee_evaluation_system.service;

import com.dongnv.employee_evaluation_system.dto.mapper.DepartmentMapper;
import com.dongnv.employee_evaluation_system.dto.request.DepartmentDTO;
import com.dongnv.employee_evaluation_system.model.Department;
import com.dongnv.employee_evaluation_system.repository.DepartmentRepository;
import com.dongnv.employee_evaluation_system.repository.EmployeeRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class DepartmentService {
    DepartmentRepository departmentRepository;
    EmployeeRepository employeeRepository;
    DepartmentMapper departmentMapper;

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public Page<Department> getDepartmentsByPage(int page) {
        Page<Department> departmentList = departmentRepository.findAll(PageRequest.of(page, 10));
        departmentList.getContent().forEach(d -> d.setCountEmployee(employeeRepository.countByDepartmentId(d.getId())));
        return departmentList;
    }

    public Department getDepartmentById(Integer id) {
        return departmentRepository.findById(id).orElse(null);
    }

    public void saveDepartment(Department department) {
        departmentRepository.save(department);
    }

    public void updateDepartment(DepartmentDTO departmentDTO, Integer id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found department with ID: " + id));
        departmentMapper.updatedDepartment(department, departmentDTO);
        departmentRepository.save(department);
    }

    public void deleteDepartmentById(Integer id) {
        departmentRepository.deleteById(id);
    }

}
