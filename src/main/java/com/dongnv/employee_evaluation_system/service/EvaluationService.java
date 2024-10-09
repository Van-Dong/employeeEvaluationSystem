package com.dongnv.employee_evaluation_system.service;

import com.dongnv.employee_evaluation_system.dto.mapper.EvaluationMapper;
import com.dongnv.employee_evaluation_system.dto.request.EvaluationDTO;
import com.dongnv.employee_evaluation_system.exception.AppException;
import com.dongnv.employee_evaluation_system.exception.ErrorCode;
import com.dongnv.employee_evaluation_system.model.Employee;
import com.dongnv.employee_evaluation_system.model.Evaluation;
import com.dongnv.employee_evaluation_system.repository.EmployeeRepository;
import com.dongnv.employee_evaluation_system.repository.EvaluationRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class EvaluationService {
    EvaluationRepository evaluationRepository;
    EmployeeRepository employeeRepository;
    EvaluationMapper evaluationMapper;

    public Page<Evaluation> getEvaluationByPage(Integer page) {
        return evaluationRepository.findAll(PageRequest.of(page, 10, Sort.by(Sort.Order.desc("evaluationDate"))));
    }

    public Page<Evaluation> getEvaluationByEmployeeId(Long employeeId, Integer page) {
        return evaluationRepository.findByEmployeeId(employeeId,
                PageRequest.of(page, 10, Sort.by(Sort.Order.desc("evaluationDate"))));
    }

    public void createEvaluation(Long employeeId, EvaluationDTO evaluationDTO) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new AppException(ErrorCode.EMPLOYEE_NOT_FOUND));
        Evaluation evaluation = evaluationMapper.toEvaluation(evaluationDTO);
        evaluation.setEmployee(employee);
        evaluationRepository.save(evaluation);
    }

    public void updateEvaluation(Long id, EvaluationDTO evaluationDTO) {
        Evaluation evaluation = evaluationRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.EVALUATION_NOT_FOUND));
        evaluationMapper.updatedEvaluation(evaluation, evaluationDTO);
        evaluationRepository.save(evaluation);
    }

    public void deleteEvaluationById(Long id) {
        evaluationRepository.deleteById(id);
    }
    
}
