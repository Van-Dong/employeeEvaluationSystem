package com.dongnv.employee_evaluation_system.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.dongnv.employee_evaluation_system.constant.UserRole;
import com.dongnv.employee_evaluation_system.dto.mapper.UserMapper;
import com.dongnv.employee_evaluation_system.dto.mapper.UserMapperImpl;
import com.dongnv.employee_evaluation_system.dto.request.SetRoleRequest;
import com.dongnv.employee_evaluation_system.dto.request.UserCreationRequest;
import com.dongnv.employee_evaluation_system.dto.response.UserResponse;
import com.dongnv.employee_evaluation_system.exception.AppException;
import com.dongnv.employee_evaluation_system.exception.ErrorCode;
import com.dongnv.employee_evaluation_system.model.User;
import com.dongnv.employee_evaluation_system.repository.UserRepository;

// Use Mock And Spy
class UserServiceTest {
    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Spy
    UserMapper userMapper;

    @InjectMocks
    UserService userService;

    @BeforeEach
    void setUp() {
        userMapper = new UserMapperImpl();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getUserByPage() {
        // GIVEN
        String searchUsername = "user";
        int page = 0;

        User user1 =
                User.builder().id(1L).username("user1").role(UserRole.CUSTOMER).build();
        User user2 =
                User.builder().id(2L).username("user2").role(UserRole.CUSTOMER).build();
        List<User> users = List.of(user1, user2);
        Page<User> userPage = new PageImpl<>(users, PageRequest.of(page, 10), users.size());

        Mockito.when(userRepository.findAllByUsernameLike(
                        "%" + searchUsername + "%", PageRequest.of(page, 10, Sort.by(Sort.Order.desc("createdDate")))))
                .thenReturn(userPage);

        // WHEN
        Page<UserResponse> userResponsePage = userService.getUserByPage(searchUsername, page);

        // THEN
        Assertions.assertEquals(userPage.getTotalElements(), userResponsePage.getTotalElements());
        Assertions.assertEquals(
                user1.getId(), userResponsePage.getContent().get(0).getId());
        Mockito.verify(userRepository, Mockito.times(1)).findAllByUsernameLike(anyString(), any(PageRequest.class));
    }

    @Test
    void createUser_valid_success() {
        // GIVEN
        UserCreationRequest userCreationRequest = UserCreationRequest.builder()
                .username("user")
                .password("password")
                .build();
        Mockito.when(passwordEncoder.encode("password")).thenReturn("password-hash");
        Mockito.when(passwordEncoder.matches("password", "password-hash")).thenReturn(true);

        // WHEN
        userService.createUser(userCreationRequest);

        // THEN
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        Mockito.verify(userRepository, Mockito.times(1)).save(userArgumentCaptor.capture());
        User savedUser = userArgumentCaptor.getValue();
        Assertions.assertEquals(userCreationRequest.getUsername(), savedUser.getUsername());
        Assertions.assertTrue(passwordEncoder.matches(userCreationRequest.getPassword(), savedUser.getPassword()));
    }

    @Test
    void createUser_usernameExist_fail() {
        // GIVEN
        UserCreationRequest userCreationRequest = UserCreationRequest.builder()
                .username("user")
                .password("password")
                .build();
        Mockito.doThrow(new DataIntegrityViolationException("Username is duplicate"))
                .when(userRepository)
                .save(any(User.class));

        // WHEN, THEN
        Assertions.assertThrows(
                DataIntegrityViolationException.class, () -> userService.createUser(userCreationRequest));
    }

    @Test
    void setNewPassword_valid_success() {
        // GIVEN
        long id = 1;
        String newPassword = "newPassword";
        User user =
                User.builder().id(id).username("user").role(UserRole.CUSTOMER).build();
        Mockito.when(userRepository.findById(id)).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.encode("newPassword")).thenReturn("newPassword-hash");
        Mockito.when(passwordEncoder.matches("newPassword", "newPassword-hash")).thenReturn(true);

        // WHEN
        userService.setNewPassword(id, newPassword);

        // THEN
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        Mockito.verify(userRepository, Mockito.times(1)).save(userArgumentCaptor.capture());
        User savedUser = userArgumentCaptor.getValue();
        Assertions.assertEquals(id, savedUser.getId());
        Assertions.assertTrue(passwordEncoder.matches(newPassword, savedUser.getPassword()));
    }

    @Test
    void setNewPassword_userNotExist_fail() {
        // GIVEN
        long id = 1;
        String newPassword = "newPassword";
        Mockito.when(userRepository.findById(id)).thenReturn(Optional.ofNullable(null));

        // WHEN, THEN
        var exception = Assertions.assertThrows(AppException.class, () -> userService.setNewPassword(id, newPassword));
        Assertions.assertEquals(
                ErrorCode.USER_NOT_FOUND.getCode(), exception.getErrorCode().getCode());
    }

    @Test
    void setNewRole_valid_success() {
        // GIVEN
        long id = 1;
        SetRoleRequest request =
                SetRoleRequest.builder().role(UserRole.MANAGER.name()).build();
        User user =
                User.builder().id(id).username("user").role(UserRole.CUSTOMER).build();
        Mockito.when(userRepository.findById(id)).thenReturn(Optional.of(user));

        // WHEN
        userService.setNewRole(id, request);

        // THEN
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        Mockito.verify(userRepository, Mockito.times(1)).save(userArgumentCaptor.capture());
        User savedUser = userArgumentCaptor.getValue();
        Assertions.assertEquals(UserRole.MANAGER, user.getRole());
    }

    @Test
    void setNewRole_userNotExist_fail() {
        // GIVEN
        long id = 1;
        SetRoleRequest request =
                SetRoleRequest.builder().role(UserRole.MANAGER.name()).build();
        Mockito.when(userRepository.findById(id)).thenReturn(Optional.ofNullable(null));

        // WHEN, THEN
        var exception = Assertions.assertThrows(AppException.class, () -> userService.setNewRole(id, request));
        Assertions.assertEquals(
                ErrorCode.USER_NOT_FOUND.getCode(), exception.getErrorCode().getCode());
    }

    @Test
    void deleteUser() {
        // GIVEN
        long id = 1;

        // WHEN
        userService.deleteUser(id);

        // THEN
        Mockito.verify(userRepository, Mockito.times(1)).deleteById(id);
    }
}
