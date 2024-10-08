package com.dongnv.employee_evaluation_system.controller;

import com.dongnv.employee_evaluation_system.dto.request.DepartmentDTO;
import com.dongnv.employee_evaluation_system.dto.request.EmployeeDTO;
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
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/employee")
@Slf4j
public class EmployeeController {
    EmployeeService employeeService;
    DepartmentService departmentService;

    @GetMapping
    String getAllEmployees(@RequestParam(defaultValue = "0") Integer page, Model model) {
        if (page < 0) page = 0;
        Page<Employee> employeePage = employeeService.getEmployeesByPage(page);
        model.addAttribute("employeePage", employeePage);
        return "employee/index";
    }

    @GetMapping("/department/{departmentId}")
    String getEmployeeByDepartmentId(@PathVariable Integer departmentId, @RequestParam(defaultValue = "0") Integer page, Model model) {
        if (page < 0) page = 0;
        DepartmentDTO department = departmentService.getDepartmentById(departmentId);
        Page<Employee> employeePage = employeeService.getEmployeesByDepartmentId(departmentId, page);
        model.addAttribute("employeePage", employeePage);
        model.addAttribute("department", department);
        return "employee/index";
    }

    @GetMapping("/create")
    String createEmployee(Model model) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        List<Department> departments = departmentService.getAllDepartments();
        model.addAttribute("departments", departments);
        model.addAttribute("employeeDTO", employeeDTO);
        return "employee/add-employee";
    }

    @PostMapping("/save")
    String createEmployee(@Valid EmployeeDTO employeeDTO, BindingResult result, Model model) {
        if (result.hasErrors()) {
            List<Department> departments = departmentService.getAllDepartments();
            model.addAttribute("departments", departments);
            return "employee/add-employee";
        }
        log.info("EMPLOYEE DTO: " + employeeDTO);
        employeeService.save(employeeDTO);
    return "redirect:/employee";
}

    @GetMapping("/edit/{id}")
    String editEmployee(@PathVariable long id, Model model, RedirectAttributes redirectAttributes) {
        EmployeeDTO employeeDTO = employeeService.getEmployeeById(id);
        List<Department> departments = departmentService.getAllDepartments();
        model.addAttribute("departments", departments);
        model.addAttribute("employeeDTO", employeeDTO);
        return "employee/edit-employee";
    }

    @PostMapping("/update/{id}")
    String updateEmployee(@PathVariable long id, @Valid EmployeeDTO employeeDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            List<Department> departments = departmentService.getAllDepartments();
            model.addAttribute("departments", departments);
            return "employee/edit-employee";
        }
        employeeService.updateEmployee(employeeDTO, id);
        return "redirect:/employee";
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    ResponseEntity<Void> deleteEmployee(@PathVariable long id) {
        employeeService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
