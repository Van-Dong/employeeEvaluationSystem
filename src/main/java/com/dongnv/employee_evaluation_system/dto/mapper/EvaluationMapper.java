package com.dongnv.employee_evaluation_system.dto.mapper;

import com.dongnv.employee_evaluation_system.dto.request.EvaluationDTO;
import com.dongnv.employee_evaluation_system.model.Evaluation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface EvaluationMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "evaluationDate", ignore = true)
    Evaluation toEvaluation(EvaluationDTO evaluationDTO);

    EvaluationDTO toEvaluationDTO(Evaluation evaluation);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "evaluationDate", ignore = true)
    void updatedEvaluation(@MappingTarget Evaluation evaluation, EvaluationDTO evaluationDTO);
}
