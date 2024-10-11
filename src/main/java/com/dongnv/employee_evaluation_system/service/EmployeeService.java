package com.dongnv.employee_evaluation_system.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.dongnv.employee_evaluation_system.dto.mapper.EmployeeMapper;
import com.dongnv.employee_evaluation_system.dto.request.EmployeeDTO;
import com.dongnv.employee_evaluation_system.exception.AppException;
import com.dongnv.employee_evaluation_system.exception.ErrorCode;
import com.dongnv.employee_evaluation_system.model.Department;
import com.dongnv.employee_evaluation_system.model.Employee;
import com.dongnv.employee_evaluation_system.repository.DepartmentRepository;
import com.dongnv.employee_evaluation_system.repository.EmployeeRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class EmployeeService {
    EmployeeRepository employeeRepository;
    DepartmentRepository departmentRepository;
    EmployeeMapper employeeMapper;
    FileStorageService fileStorageService;

    public Page<Employee> getEmployeesByPage(String searchName, Integer page) {
        return employeeRepository.findAllByFullNameLike("%" + searchName + "%", PageRequest.of(page, 10));
    }

    public Page<Employee> getEmployeesByDepartmentId(Integer departmentId, Integer page) {
        return employeeRepository.findByDepartmentId(departmentId, PageRequest.of(page, 10));
    }

    public EmployeeDTO getEmployeeById(Long id) {
        Employee employee =
                employeeRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.EMPLOYEE_NOT_FOUND));
        EmployeeDTO employeeDTO = employeeMapper.toEmployeeDTO(employee);

        if (employee.getDepartment() != null) {
            employeeDTO.setDepartmentId(employee.getDepartment().getId());
        }
        return employeeDTO;
    }

    public void save(EmployeeDTO employeeDTO) {
        Employee employee = employeeMapper.toEmployee(employeeDTO);

        // Store image file
        if (!(employeeDTO.getImageFile() == null || employeeDTO.getImageFile().isEmpty())) {
            String imageUrl = fileStorageService.storeFile(employeeDTO.getImageFile());
            employee.setImageUrl(imageUrl);
        }

        // Set department
        if (employeeDTO.getDepartmentId() != null) {
            Department department = departmentRepository
                    .findById(employeeDTO.getDepartmentId())
                    .orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_FOUND));
            employee.setDepartment(department);
        }

        employeeRepository.save(employee);
    }

    public void updateEmployee(EmployeeDTO employeeDTO, Long id) {
        Employee employee =
                employeeRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.EMPLOYEE_NOT_FOUND));
        employeeMapper.updatedEmployee(employee, employeeDTO);

        // Store image file
        if (!(employeeDTO.getImageFile() == null || employeeDTO.getImageFile().isEmpty())) {
            String imageUrl = fileStorageService.storeFile(employeeDTO.getImageFile());
            if (employee.getImageUrl() != null) {
                fileStorageService.deleteFile(employee.getImageUrl());
            }
            employee.setImageUrl(imageUrl);
        }

        // Set department
        if (employeeDTO.getDepartmentId() != null) {
            Department department = departmentRepository
                    .findById(employeeDTO.getDepartmentId())
                    .orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_FOUND));
            employee.setDepartment(department);
        }

        employeeRepository.save(employee);
    }
    ;

    public void deleteById(Long id) {
        employeeRepository.deleteById(id);
    }
}
