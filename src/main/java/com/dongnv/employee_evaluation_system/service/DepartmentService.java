package com.dongnv.employee_evaluation_system.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.dongnv.employee_evaluation_system.dto.mapper.DepartmentMapper;
import com.dongnv.employee_evaluation_system.dto.request.DepartmentDTO;
import com.dongnv.employee_evaluation_system.exception.AppException;
import com.dongnv.employee_evaluation_system.exception.ErrorCode;
import com.dongnv.employee_evaluation_system.model.Department;
import com.dongnv.employee_evaluation_system.repository.DepartmentRepository;
import com.dongnv.employee_evaluation_system.repository.EmployeeRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

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

    public Page<Department> getDepartmentsByPage(int page, String searchName) {
        Page<Department> departmentList =
                departmentRepository.findAllByNameLike("%" + searchName + "%", PageRequest.of(page, 10));
        departmentList.getContent().forEach(d -> d.setCountEmployee(employeeRepository.countByDepartmentId(d.getId())));
        return departmentList;
    }

    public DepartmentDTO getDepartmentById(Integer id) {
        Department department =
                departmentRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_FOUND));
        return departmentMapper.toDepartmentDTO(department);
    }

    public void createDepartment(DepartmentDTO departmentDTO) {
        Department department = departmentMapper.toDepartment(departmentDTO);
        departmentRepository.save(department);
    }

    public void updateDepartment(DepartmentDTO departmentDTO, Integer id) {
        Department department =
                departmentRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_FOUND));
        departmentMapper.updatedDepartment(department, departmentDTO);
        departmentRepository.save(department);
    }

    public void deleteDepartmentById(Integer id) {
        departmentRepository.deleteById(id);
    }
}
