package com.dongnv.employee_evaluation_system.controller;

import com.dongnv.employee_evaluation_system.model.Department;
import com.dongnv.employee_evaluation_system.model.Employee;
import com.dongnv.employee_evaluation_system.service.DepartmentService;
import com.dongnv.employee_evaluation_system.service.EmployeeService;
import com.dongnv.employee_evaluation_system.service.FileStorageService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/employee")
@Slf4j
public class EmployeeController {
    EmployeeService employeeService;
    FileStorageService fileStorageService;
    DepartmentService departmentService;

    @GetMapping
    String getAllEmployee(Model model) {
        List<Employee> employees = employeeService.getAll();
        model.addAttribute("employees", employees);
        return "employee/index";
    }

    @GetMapping("/create")
    String createEmployee(Model model) {
        Employee employee = new Employee();
        List<Department> departments = departmentService.getAllDepartments();
        model.addAttribute("departments", departments);
        model.addAttribute("employee", employee);
        return "employee/add-employee";
    }

    @PostMapping("/save")
    String saveEmployee(@RequestParam("imageFile") MultipartFile file, @RequestParam Integer departmentId, Employee employee, BindingResult result) {
        try {
            String imagePath = fileStorageService.storeFile(file);
            employee.setImageUrl(imagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (result.hasErrors()) {
            return "employee/add-employee";
        }
        employee.setDepartment(departmentService.getDepartmentById(departmentId));
    employeeService.save(employee);
    return "redirect:/employee";
}

    @GetMapping("/edit/{id}")
    String editEmployee(@PathVariable long id, Model model) {
        Employee employee = employeeService.getById(id);
        List<Department> departments = departmentService.getAllDepartments();
        model.addAttribute("departments", departments);
        model.addAttribute("employee", employee);
        return "employee/edit-employee";
    }

    @PostMapping("/update/{id}")
    String updateEmployee(@RequestParam("imageFile") MultipartFile file, @PathVariable long id, @Valid Employee employee, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "employee/edit-employee";
        }

        Employee employeeOld = employeeService.getById(id);

        if (file == null || file.isEmpty()) {
            employee.setImageUrl(employeeOld.getImageUrl());
        } else {
            try {
                String imagePath = fileStorageService.storeFile(file);
                employee.setImageUrl(imagePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        employeeService.save(employee);
        return "redirect:/employee";
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    ResponseEntity<Void> deleteEmployee(@PathVariable long id) {
        employeeService.deleteById(id);
        return ResponseEntity.ok().build();
    }

}
