package com.dongnv.employee_evaluation_system.service;

import com.dongnv.employee_evaluation_system.repository.EvaluationRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class EvaluationService {
    EvaluationRepository evaluationRepository;

}
