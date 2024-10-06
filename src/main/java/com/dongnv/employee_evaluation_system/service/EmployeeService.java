package com.dongnv.employee_evaluation_system.service;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class EmployeeService {
    EmployeeRepository employeeRepository;
    DepartmentRepository departmentRepository;
    EmployeeMapper employeeMapper;
    FileStorageService fileStorageService;

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Page<Employee> getEmployeesByPage(Integer page) {
        return employeeRepository.findAll(PageRequest.of(page, 10));
    }

    public EmployeeDTO getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.EMPLOYEE_NOT_FOUND));
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
            try {
                String imageUrl = fileStorageService.storeFile(employeeDTO.getImageFile());
                employee.setImageUrl(imageUrl);
            } catch (IOException e) {
                throw new AppException(ErrorCode.ERROR_WHEN_STORE_IMAGE);
            }
        }

        // Set department
        if (employeeDTO.getDepartmentId() != null) {
            Department department = departmentRepository.findById(employeeDTO.getDepartmentId())
                    .orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_FOUND));
            employee.setDepartment(department);
        }

        employeeRepository.save(employee);
    }

    public void updateEmployee(EmployeeDTO employeeDTO, Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.EMPLOYEE_NOT_FOUND));
        employeeMapper.updatedEmployee(employee, employeeDTO);

        // Set department
        if (employeeDTO.getDepartmentId() != null) {
            Department department = departmentRepository.findById(employeeDTO.getDepartmentId())
                    .orElseThrow(() -> new AppException(ErrorCode.ERROR_WHEN_STORE_IMAGE));
            employee.setDepartment(department);
        }

        // Store image file
        if (!(employeeDTO.getImageFile() == null || employeeDTO.getImageFile().isEmpty())) {
            try {
                String imageUrl = fileStorageService.storeFile(employeeDTO.getImageFile());
                employee.setImageUrl(imageUrl);
            } catch (IOException e) {
                throw new AppException(ErrorCode.ERROR_WHEN_STORE_IMAGE);
            }
        }

        employeeRepository.save(employee);
    };

    public void deleteById(Long id) {
        employeeRepository.deleteById(id);
    }
}
