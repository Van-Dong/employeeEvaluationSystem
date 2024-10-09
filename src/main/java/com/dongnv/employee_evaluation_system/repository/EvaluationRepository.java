package com.dongnv.employee_evaluation_system.repository;

import com.dongnv.employee_evaluation_system.dto.response.DepartmentScore;
import com.dongnv.employee_evaluation_system.dto.response.EmployeeScore;
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

    // Get employee with high score
    @Query("SELECT new com.dongnv.employee_evaluation_system.dto.response.EmployeeScore(e.employee.id, " +
            "COUNT(CASE WHEN e.isCommended = true THEN 1 ELSE null END), " +
            "COUNT(CASE When e.isCommended = false THEN 1 ELSE null END)) " +
            "FROM Evaluation e " +
            "WHERE e.employee.fullName LIKE :employeeName " +
            "GROUP BY e.employee.id " +
            "ORDER BY (COUNT(CASE WHEN e.isCommended = true THEN 1 ELSE null END) - " +
             "COUNT(CASE When e.isCommended = false THEN 1 ELSE null END)) DESC"
    )
    Page<EmployeeScore> personalAchievementsByEmployeeName(@Param("employeeName") String employeeName, Pageable pageable);

    // Get employee with high score
    @Query("SELECT new com.dongnv.employee_evaluation_system.dto.response.DepartmentScore(d.id, d.code, d.name, " +
            "COUNT(CASE WHEN eval.isCommended = true THEN 1 ELSE null END), " +
            "COUNT(CASE WHEN eval.isCommended = false THEN 1 ELSE null END)) " +
            "FROM Evaluation eval JOIN eval.employee e JOIN e.department d " +
            "WHERE d.name LIKE :departmentName " +
            "GROUP BY d.id " +
            "ORDER BY (COUNT(CASE WHEN eval.isCommended = true THEN 1 ELSE null END) - " +
            "COUNT(CASE When eval.isCommended = false THEN 1 ELSE null END)) DESC"

    )
    Page<DepartmentScore> departmentAchievementsByNameLike(@Param("departmentName") String departmentName, Pageable pageable);
}
