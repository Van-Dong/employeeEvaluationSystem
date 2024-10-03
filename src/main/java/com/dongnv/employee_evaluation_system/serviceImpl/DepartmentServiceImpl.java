package com.dongnv.employee_evaluation_system.serviceImpl;

import com.dongnv.employee_evaluation_system.model.Department;
import com.dongnv.employee_evaluation_system.repository.DepartmentRepository;
import com.dongnv.employee_evaluation_system.service.DepartmentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {
    DepartmentRepository departmentRepository;

    @Override
    public List<Department> getAll() {
        return departmentRepository.findAll();
    }

    @Override
    public Department getById(Long id) {
        return departmentRepository.findById(id).orElse(null);
    }

    @Override
    public Department save(Department department) {
        return departmentRepository.save(department);
    }

    @Override
    public void deleteById(Long id) {
        departmentRepository.deleteById(id);
    }

    @Override
    public boolean existsByName(String name) {
        return departmentRepository.existsByName(name);
    }

    @Override
    public boolean existsByCode(String code) {
        return departmentRepository.existsByCode(code);
    }
}
