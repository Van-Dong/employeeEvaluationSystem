package com.dongnv.employee_evaluation_system.controller;

import com.dongnv.employee_evaluation_system.dto.request.DepartmentDTO;
import com.dongnv.employee_evaluation_system.model.Department;
import com.dongnv.employee_evaluation_system.service.DepartmentService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/department")
@Slf4j
public class DepartmentController {
    DepartmentService departmentService;

    @GetMapping
    public String getAllDepartments(@RequestParam(defaultValue = "0") int page, Model model) {
        if (page < 0) page = 0;
        Page<Department> departmentPage = departmentService.getDepartmentsByPage(page);
        model.addAttribute("departmentPage", departmentPage);
        return "department/index";
    }

    @GetMapping("/create")
    public String createDepartment(Model model) {
        model.addAttribute("department", new Department());
        return "department/add-department";
    }

    @PostMapping("/save")
    public String createDepartment(@Valid Department department, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "department/add-department";
        }
        try {
            departmentService.saveDepartment(department);
        } catch (DataIntegrityViolationException exception) {
            model.addAttribute("errorMessage", "Duplicate name or code.");
            return "department/add-department";
        }

        return "redirect:/department";
    }

    @GetMapping("/edit/{id}")
    public String editDepartment(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        Department department = departmentService.getDepartmentById(id);
        if (department == null) {
            redirectAttributes.addFlashAttribute("message", "Not found department with ID: " + id);
            return "redirect:/department";
        }
        model.addAttribute("department", department);
        return "department/edit-department";
    }

    @PostMapping("/update/{id}")
    public String updateDepartment(@Valid DepartmentDTO departmentDTO, @PathVariable Integer id, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "department/edit-department";
        }
        log.info("DEPARTMENTDTO: " + departmentDTO);
        try {
            departmentService.updateDepartment(departmentDTO, id);
        } catch (ConstraintViolationException e) {
            model.addAttribute("errorMessage", "Duplicate name or code.");
            return "department/edit-department";
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "department/edit-department";
        }
        return "redirect:/department";
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteDepartment(@PathVariable Integer id) {
        departmentService.deleteDepartmentById(id);
        return ResponseEntity.ok().build();
    }
}
