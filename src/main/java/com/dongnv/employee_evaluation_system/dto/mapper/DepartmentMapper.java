package com.dongnv.employee_evaluation_system.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.dongnv.employee_evaluation_system.dto.request.DepartmentDTO;
import com.dongnv.employee_evaluation_system.model.Department;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {
    @Mapping(target = "id", ignore = true)
    void updatedDepartment(@MappingTarget Department department, DepartmentDTO departmentDTO);

    Department toDepartment(DepartmentDTO departmentDTO);

    DepartmentDTO toDepartmentDTO(Department department);
}
