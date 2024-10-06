package com.dongnv.employee_evaluation_system.service;

import com.dongnv.employee_evaluation_system.dto.mapper.UserMapper;
import com.dongnv.employee_evaluation_system.dto.request.UserDTO;
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

    public Page<User> getUserByPage(Integer page) {
        return userRepository.findAll(PageRequest.of(page, 10,
                Sort.by(Sort.Order.desc("createdDate"))));
    }

    // register, have exception if duplicate username -> need handle
    public void createUser(UserDTO userDTO) {
        User user = userMapper.toUser(userDTO);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        userRepository.save(user);
    }

    public void activeUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        if (!user.getIsActive()) {
            user.setIsActive(true);
        }
        userRepository.save(user);
    }

    public void deactiveUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        if (user.getIsActive()) {
            user.setIsActive(false);
        }
        userRepository.save(user);
    }

    // By Admin
    public void setNewPassword(Long id, String newPassword) {
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
