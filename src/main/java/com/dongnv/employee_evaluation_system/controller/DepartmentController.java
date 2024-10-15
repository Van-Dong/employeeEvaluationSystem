package com.dongnv.employee_evaluation_system.controller;

import jakarta.validation.Valid;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.dongnv.employee_evaluation_system.dto.request.DepartmentDTO;
import com.dongnv.employee_evaluation_system.model.Department;
import com.dongnv.employee_evaluation_system.service.DepartmentService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Controller
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/department")
@Slf4j
public class DepartmentController {
    DepartmentService departmentService;

    @GetMapping
    public String getAllDepartments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "") String searchName,
            Model model) {
        if (page < 0) page = 0;
        Page<Department> departmentPage = departmentService.getDepartmentsByPage(page, searchName);
        model.addAttribute("departmentPage", departmentPage);
        return "department/index";
    }

    @GetMapping("/create")
    public String createDepartment(Model model) {
        model.addAttribute("departmentDTO", new DepartmentDTO());
        return "department/add-department";
    }

    @PostMapping("/save")
    public String createDepartment(@Valid DepartmentDTO departmentDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "department/add-department";
        }

        // Catch duplicate name or code exception to inform error
        try {
            departmentService.createDepartment(departmentDTO);
        } catch (DataIntegrityViolationException exception) {
            model.addAttribute("errorMessage", "Duplicate name or code.");
            return "department/add-department";
        }

        return "redirect:/department";
    }

    @GetMapping("/edit/{id}")
    public String editDepartment(@PathVariable Integer id, Model model) {
        DepartmentDTO departmentDTO = departmentService.getDepartmentById(id);
        model.addAttribute("departmentDTO", departmentDTO);
        return "department/edit-department";
    }

    @PostMapping("/update/{id}")
    public String updateDepartment(
            @PathVariable Integer id, @Valid DepartmentDTO departmentDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "department/edit-department";
        }

        // Catch duplicate name or code exception to inform error
        try {
            departmentService.updateDepartment(departmentDTO, id);
        } catch (ConstraintViolationException e) {
            model.addAttribute("errorMessage", "Duplicate name or code.");
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
