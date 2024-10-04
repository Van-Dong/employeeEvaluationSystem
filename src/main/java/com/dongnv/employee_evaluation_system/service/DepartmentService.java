package com.dongnv.employee_evaluation_system.service;

import com.dongnv.employee_evaluation_system.model.Department;
import com.dongnv.employee_evaluation_system.repository.DepartmentRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class DepartmentService {
    DepartmentRepository departmentRepository;

    public List<Department> getAll() {
        return departmentRepository.findAll();
    }

    public Page<Department> findPaginated(int page) {
        return departmentRepository.findAll(PageRequest.of(page, 10));
    }

    public Department getById(Long id) {
        return departmentRepository.findById(id).orElse(null);
    }

    public void save(Department department) {
        departmentRepository.save(department);
    }

    public void deleteById(Long id) {
        departmentRepository.deleteById(id);
    }

    public boolean existsByName(String name) {
        return departmentRepository.existsByName(name);
    }

    public boolean existsByCode(String code) {
        return departmentRepository.existsByCode(code);
    }
}
