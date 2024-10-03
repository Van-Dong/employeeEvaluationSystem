package com.dongnv.employee_evaluation_system.repository;

import com.dongnv.employee_evaluation_system.model.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {
}
