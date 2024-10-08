package com.dongnv.employee_evaluation_system.controller;

import com.dongnv.employee_evaluation_system.dto.response.DepartmentScore;
import com.dongnv.employee_evaluation_system.dto.response.EmployeeScore;
import com.dongnv.employee_evaluation_system.service.RankingService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/")
public class HomeController {
    RankingService rankingService;

    @GetMapping
    String getBestEmployee(Model model) {
        Page<EmployeeScore> employeeScorePage = rankingService.getEmployeeScore(0);
        model.addAttribute("employeeScorePage", employeeScorePage);
        return "main/home";
    }

    @GetMapping("/ranking/employee")
    String getRankingEmployee(@RequestParam(defaultValue = "0") Integer page, Model model) {
        if (page < 0) page = 0;
        Page<EmployeeScore> employeeScorePage = rankingService.getEmployeeScore(page);
        model.addAttribute("employeeScorePage", employeeScorePage);
        return "main/ranking-employee";
    }

    @GetMapping("/ranking")
    String getRankingDepartment(@RequestParam(defaultValue = "0") Integer page, Model model) {
        if (page < 0) page = 0;
        Page<DepartmentScore> departmentScorePage = rankingService.getDepartmentScore(page);
        model.addAttribute("departmentScorePage", departmentScorePage);
        return "main/ranking-department";
    }

    @GetMapping("/error/access-denied")
    String getErrorAccessDenied() {
        return "error/access-denied";
    }
}
