package com.dongnv.employee_evaluation_system.service;

import com.dongnv.employee_evaluation_system.constant.UserRole;
import com.dongnv.employee_evaluation_system.dto.mapper.UserMapper;
import com.dongnv.employee_evaluation_system.dto.request.SetRoleRequest;
import com.dongnv.employee_evaluation_system.dto.request.UserCreationRequest;
import com.dongnv.employee_evaluation_system.dto.response.UserResponse;
import com.dongnv.employee_evaluation_system.exception.AppException;
import com.dongnv.employee_evaluation_system.exception.ErrorCode;
import com.dongnv.employee_evaluation_system.model.User;
import com.dongnv.employee_evaluation_system.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class UserService {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    UserMapper userMapper;

    public Page<UserResponse> getUserByPage(Integer page) {
        return userRepository.findAll(PageRequest.of(page, 10,
                Sort.by(Sort.Order.desc("createdDate")))).map(userMapper::toUserResponse);
    }

    // register, have exception if duplicate username -> need handle
    public void createUser(UserCreationRequest userCreationRequest) {
        User user = User.builder().username(userCreationRequest.getUsername()).build();
        user.setPassword(passwordEncoder.encode(userCreationRequest.getPassword()));
        userRepository.save(user);
    }

    // By Admin
    public void setNewPassword(Long id, String newPassword) {
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    // Set Role by Admin
    public void setNewRole(Long id, SetRoleRequest request) {
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        user.setRole(UserRole.valueOf(request.getRole()));
        userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
