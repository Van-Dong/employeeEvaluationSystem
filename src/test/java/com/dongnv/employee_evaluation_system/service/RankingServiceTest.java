package com.dongnv.employee_evaluation_system.service;

import com.dongnv.employee_evaluation_system.dto.response.EmployeeScore;
import com.dongnv.employee_evaluation_system.model.Employee;
import com.dongnv.employee_evaluation_system.repository.EmployeeRepository;
import com.dongnv.employee_evaluation_system.repository.EvaluationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

class RankingServiceTest {
    @Mock
    EvaluationRepository evaluationRepository;

    @Mock
    EmployeeRepository employeeRepository;

    @InjectMocks
    RankingService rankingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getEmployeeScore() {
        // GIVEN
        // create Page<EmployeeScore>
        EmployeeScore employeeScore1 = EmployeeScore.builder()
                .id(1L)
                .totalRewards(10L)
                .totalDisciplines(3L)
                .build();
        EmployeeScore employeeScore3 = EmployeeScore.builder()
                .id(2L)
                .totalRewards(7L)
                .totalDisciplines(5L)
                .build();
        List<Employee> employeeList = List.of(Employee.builder().id(1L).fullName("Nguyen Van A").build(),
                Employee.builder().id(2L).fullName("Nguyen Van B").build());

        List<EmployeeScore> employeeScoreList = List.of(employeeScore1, employeeScore3);


        int page = 0;
        String searchName = "user";
        Page<EmployeeScore> employeeScorePage = new PageImpl<>(employeeScoreList, PageRequest.of(page, 10), employeeScoreList.size());

        Mockito.when(evaluationRepository.personalAchievementsByEmployeeName("%" + searchName + "%", PageRequest.of(page, 10)))
                .thenReturn(employeeScorePage);
        Mockito.when(employeeRepository.findAllByIdIn(any())).thenReturn(employeeList);

        // WHEN
        rankingService.getEmployeeScore(searchName, page);

        // THEN
        Assertions.assertEquals(employeeScoreList.size(), employeeScorePage.getTotalElements());
        Assertions.assertSame(employeeScorePage.getContent().get(0).getEmployee().getId(), employeeScorePage.getContent().get(0).getId());
    }

    @Test
    void getDepartmentScore() {
        // GIVEN
        String searchName = "user";
        int page = 0;

        // WHEN
        rankingService.getDepartmentScore(searchName, page);

        // THEN
        Mockito.verify(evaluationRepository, Mockito.times(1)).departmentAchievementsByNameLike("%" + searchName + "%", PageRequest.of(page, 10));
    }
}