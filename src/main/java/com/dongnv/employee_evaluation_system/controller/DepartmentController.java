package com.dongnv.employee_evaluation_system.controller;

import com.dongnv.employee_evaluation_system.model.Department;
import com.dongnv.employee_evaluation_system.service.DepartmentService;
import com.dongnv.employee_evaluation_system.serviceImpl.DepartmentServiceImpl;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

@Controller
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/department")
@Slf4j
public class DepartmentController {
    DepartmentService departmentService;

    @GetMapping
    public String getAllDepartments(Model model) {
        List<Department> departments = departmentService.getAll();
        model.addAttribute("departments", departments);
        return "department/index";
    }

    @GetMapping("/create")
    public String addDepartment(Model model) {
        model.addAttribute("department", new Department());
        return "department/add-department";
    }

    @PostMapping("/save")
    public String addDepartment(@Valid Department department, BindingResult bindingResult, Model model) {
        if (departmentService.existsByCode(department.getCode())) {
            bindingResult.rejectValue("code", "error.department", "Department code already exist");
        }
        if (bindingResult.hasErrors()) {
            return "department/add-department";
        }
        departmentService.save(department);
        return "redirect:/department";
    }

    @GetMapping("/edit/{id}")
    public String editDepartment(@PathVariable Long id, Model model) {
        Department department = departmentService.getById(id);
        if (department == null) return "redirect:/department";
        model.addAttribute("department", department);
        return "department/edit-department";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteDepartment(@PathVariable long id) {
        departmentService.deleteById(id);
        return "redirect:/department";
    }
}
