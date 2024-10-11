package com.dongnv.employee_evaluation_system.dto.mapper;

import org.mapstruct.Mapper;

import com.dongnv.employee_evaluation_system.dto.request.UserCreationRequest;
import com.dongnv.employee_evaluation_system.dto.response.UserResponse;
import com.dongnv.employee_evaluation_system.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest userCreationRequest);

    UserResponse toUserResponse(User user);
}
