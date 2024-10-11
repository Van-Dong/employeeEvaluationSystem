package com.dongnv.employee_evaluation_system.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.dongnv.employee_evaluation_system.dto.mapper.EvaluationMapper;
import com.dongnv.employee_evaluation_system.dto.mapper.EvaluationMapperImpl;
import com.dongnv.employee_evaluation_system.dto.request.EvaluationDTO;
import com.dongnv.employee_evaluation_system.exception.AppException;
import com.dongnv.employee_evaluation_system.exception.ErrorCode;
import com.dongnv.employee_evaluation_system.model.Employee;
import com.dongnv.employee_evaluation_system.model.Evaluation;
import com.dongnv.employee_evaluation_system.repository.EmployeeRepository;
import com.dongnv.employee_evaluation_system.repository.EvaluationRepository;

// Use Mock and Spy
class EvaluationServiceTest {

    @Mock
    EvaluationRepository evaluationRepository;

    @Mock
    EmployeeRepository employeeRepository;

    @Spy
    EvaluationMapper evaluationMapper;

    @InjectMocks
    EvaluationService evaluationService;

    @BeforeEach
    void setUp() {
        evaluationMapper = new EvaluationMapperImpl();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getEvaluationByPage() {
        // GIVEN
        int page = 0;

        // WHEN
        evaluationService.getEvaluationByPage(page);

        // THEN
        Mockito.verify(evaluationRepository, Mockito.times(1))
                .findAll(PageRequest.of(page, 10, Sort.by(Sort.Order.desc("evaluationDate"))));
    }

    @Test
    void getEvaluationByEmployeeId() {
        // GIVEN
        long employeeId = 0;
        int page = 0;

        // WHEN
        evaluationService.getEvaluationByEmployeeId(employeeId, page);

        // THEN
        Mockito.verify(evaluationRepository, Mockito.times(1))
                .findByEmployeeId(employeeId, PageRequest.of(page, 10, Sort.by(Sort.Order.desc("evaluationDate"))));
    }

    @Test
    void createEvaluation_valid_success() {
        // GIVEN
        long employeeId = 0;
        EvaluationDTO evaluationDTO =
                EvaluationDTO.builder().isCommended(true).reason("OT").build();
        Employee employee =
                Employee.builder().id(employeeId).fullName("Nguyen Van A").build();
        Mockito.when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));

        // WHEN
        evaluationService.createEvaluation(employeeId, evaluationDTO);

        // THEN
        ArgumentCaptor<Evaluation> argumentCaptor = ArgumentCaptor.forClass(Evaluation.class);
        Mockito.verify(evaluationRepository, Mockito.times(1)).save(argumentCaptor.capture());
        Evaluation savedEvaluation = argumentCaptor.getValue();
        Assertions.assertEquals(employee, savedEvaluation.getEmployee());
        Assertions.assertEquals(evaluationDTO.getReason(), savedEvaluation.getReason());
    }

    @Test
    void createEvaluation_employeeNotExist_fail() {
        // GIVEN
        long employeeId = 0;
        EvaluationDTO evaluationDTO =
                EvaluationDTO.builder().isCommended(true).reason("OT").build();
        Mockito.when(employeeRepository.findById(employeeId)).thenReturn(Optional.ofNullable(null));

        // WHEN, THEN
        var exception = Assertions.assertThrows(
                AppException.class, () -> evaluationService.createEvaluation(employeeId, evaluationDTO));
        Assertions.assertEquals(ErrorCode.EMPLOYEE_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void updateEvaluation_valid_success() {
        // GIVEN
        long evaluationId = 1;
        EvaluationDTO evaluationDTO =
                EvaluationDTO.builder().isCommended(false).reason("OT").build();
        Evaluation evaluation = Evaluation.builder().id(1L).isCommended(false).build();
        Mockito.when(evaluationRepository.findById(evaluationId)).thenReturn(Optional.of(evaluation));

        // WHEN
        evaluationService.updateEvaluation(evaluationId, evaluationDTO);

        // THEN
        ArgumentCaptor<Evaluation> argumentCaptor = ArgumentCaptor.forClass(Evaluation.class);
        Mockito.verify(evaluationRepository, Mockito.times(1)).save(argumentCaptor.capture());
        Evaluation savedEvaluation = argumentCaptor.getValue();
        Assertions.assertEquals(evaluationId, savedEvaluation.getId());
        Assertions.assertEquals(evaluationDTO.getReason(), savedEvaluation.getReason());
    }

    @Test
    void updateEvaluation_employeeNotExist_fail() {
        // GIVEN
        long evaluationId = 1;
        EvaluationDTO evaluationDTO =
                EvaluationDTO.builder().isCommended(false).reason("OT").build();
        Mockito.when(evaluationRepository.findById(evaluationId)).thenReturn(Optional.ofNullable(null));

        // WHEN, THEN
        var exception = Assertions.assertThrows(
                AppException.class, () -> evaluationService.updateEvaluation(evaluationId, evaluationDTO));
        Assertions.assertEquals(ErrorCode.EVALUATION_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void deleteEvaluationById() {
        // GIVEN
        long evaluationId = 1;

        // WHEN
        evaluationService.deleteEvaluationById(evaluationId);

        // THEN
        Mockito.verify(evaluationRepository, Mockito.times(1)).deleteById(evaluationId);
    }
}
