package com.dongnv.employee_evaluation_system.dto.mapper;

import com.dongnv.employee_evaluation_system.dto.request.UserCreationRequest;
import com.dongnv.employee_evaluation_system.dto.response.UserResponse;
import com.dongnv.employee_evaluation_system.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest userCreationRequest);

//    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "username", ignore = true)
//    void updatedUser(@MappingTarget User user, UserDTO userDTO);

    UserResponse toUserResponse(User user);
}
