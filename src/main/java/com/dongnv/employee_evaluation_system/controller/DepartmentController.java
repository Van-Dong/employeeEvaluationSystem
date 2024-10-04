package com.dongnv.employee_evaluation_system.controller;

import com.dongnv.employee_evaluation_system.model.Department;
import com.dongnv.employee_evaluation_system.service.DepartmentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/department")
@Slf4j
public class DepartmentController {
    DepartmentService departmentService;

    @GetMapping
    public String getAllDepartments(@RequestParam(defaultValue = "0") int page, Model model) {
//        List<Department> departments = departmentService.getAll();
        Page<Department> departmentPage = departmentService.findPaginated(page);
        model.addAttribute("departmentPage", departmentPage);
        return "department/index";
    }

    @GetMapping("/create")
    public String addDepartment(Model model) {
        model.addAttribute("department", new Department());
        return "department/add-department";
    }

    @PostMapping("/save")
    public String addDepartment(@Valid Department department, BindingResult bindingResult) {
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

    @PostMapping("/update/{id}")
    public String updateDepartment(@Valid Department department, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "department/edit-department";
        }
        try {
            departmentService.save(department);
        } catch (ConstraintViolationException e) {
            model.addAttribute("errorMessage", "Name or code already exist");
            return "department/edit-department";
        }
        return "redirect:/department";
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteDepartment(@PathVariable long id) {
        departmentService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
