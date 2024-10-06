package com.dongnv.employee_evaluation_system.repository;

import com.dongnv.employee_evaluation_system.model.Evaluation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {

    @Query("SELECT COUNT(e) FROM Evaluation e WHERE e.isCommended = :isCommended AND e.employee.id = :employeeId")
    long countEvaluationByEmployeeId(@Param("isCommended") Boolean isCommended, @Param("employeeId") Long employeeId);

    Page<Evaluation> findByEmployeeId(Long employeeId, Pageable pageable);
}
