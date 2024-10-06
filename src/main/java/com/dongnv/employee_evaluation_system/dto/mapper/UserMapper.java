package com.dongnv.employee_evaluation_system.dto.mapper;

import com.dongnv.employee_evaluation_system.dto.request.UserDTO;
import com.dongnv.employee_evaluation_system.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    User toUser(UserDTO userDTO);

//    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "username", ignore = true)
//    void updatedUser(@MappingTarget User user, UserDTO userDTO);

    @Mapping(target = "password", ignore = true)
    UserDTO toUserDTO(User user);
}
