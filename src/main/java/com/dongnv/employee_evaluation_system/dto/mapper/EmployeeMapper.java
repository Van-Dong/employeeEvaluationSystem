package com.dongnv.employee_evaluation_system.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.dongnv.employee_evaluation_system.dto.request.EmployeeDTO;
import com.dongnv.employee_evaluation_system.model.Employee;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    @Mapping(target = "imageUrl", ignore = true)
    @Mapping(target = "id", ignore = true)
    void updatedEmployee(@MappingTarget Employee employee, EmployeeDTO employeeDTO);

    @Mapping(target = "imageUrl", ignore = true)
    @Mapping(target = "id", ignore = true)
    Employee toEmployee(EmployeeDTO employeeDTO);

    EmployeeDTO toEmployeeDTO(Employee employee);
}
