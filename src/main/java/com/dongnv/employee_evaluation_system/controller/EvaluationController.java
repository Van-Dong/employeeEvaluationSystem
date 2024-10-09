package com.dongnv.employee_evaluation_system.controller;

import com.dongnv.employee_evaluation_system.dto.request.EmployeeDTO;
import com.dongnv.employee_evaluation_system.dto.request.EvaluationDTO;
import com.dongnv.employee_evaluation_system.model.Evaluation;
import com.dongnv.employee_evaluation_system.service.EmployeeService;
import com.dongnv.employee_evaluation_system.service.EvaluationService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/evaluation")
public class EvaluationController {
    EvaluationService evaluationService;
    EmployeeService employeeService;

    @GetMapping
    String getEvaluation(Model model, @RequestParam(defaultValue = "0") Integer page) {
        if (page < 0) page = 0;
        Page<Evaluation> evaluationPage = evaluationService.getEvaluationByPage(page);
        model.addAttribute("evaluationPage", evaluationPage);
        model.addAttribute("all", true);
        return "evaluation/index";
    }

    @GetMapping("/{employeeId}")
    String getEvaluation(Model model, @PathVariable long employeeId, @RequestParam(defaultValue = "0") Integer page) {
        if (page < 0) page = 0;
        EmployeeDTO e = employeeService.getEmployeeById(employeeId);
        Page<Evaluation> evaluationPage = evaluationService.getEvaluationByEmployeeId(employeeId, page);
        model.addAttribute("evaluationPage", evaluationPage);
        model.addAttribute("employee", e);
        return "evaluation/index";
    }

    @PostMapping("/create/{employeeId}")
    @ResponseBody
    ResponseEntity<String> createEvaluation(@PathVariable Long employeeId, @Valid EvaluationDTO evaluationDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder error = new StringBuilder();
            if (bindingResult.hasFieldErrors("reason")) error.append(bindingResult.getFieldError("reason").getDefaultMessage());
            return ResponseEntity.badRequest().body(error.toString());
        }
        evaluationService.createEvaluation(employeeId, evaluationDTO);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update/{id}")
    @ResponseBody
    ResponseEntity<Void> updateEvaluation(@PathVariable Long id, @Valid EvaluationDTO evaluationDTO) {
        evaluationService.updateEvaluation(id, evaluationDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    ResponseEntity<Void> deleteEvaluation(@PathVariable Long id) {
        evaluationService.deleteEvaluationById(id);
        return ResponseEntity.ok().build();
    }
}
