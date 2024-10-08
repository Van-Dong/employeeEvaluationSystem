package com.dongnv.employee_evaluation_system.controller;

import com.dongnv.employee_evaluation_system.dto.request.SetPasswordRequest;
import com.dongnv.employee_evaluation_system.dto.request.SetRoleRequest;
import com.dongnv.employee_evaluation_system.dto.request.UserCreationRequest;
import com.dongnv.employee_evaluation_system.dto.response.UserResponse;
import com.dongnv.employee_evaluation_system.service.UserService;
import jakarta.validation.Valid; 
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/user")
@Slf4j
public class UserController {
    UserService userService;

    @GetMapping
    String getUsers(@RequestParam(defaultValue = "0") Integer page, Model model) {
        Page<UserResponse> userPage = userService.getUserByPage(page);
        model.addAttribute("userPage", userPage);
        return "user/index";
    }

    @GetMapping("/register")
    String register(Model model) {
        model.addAttribute("userCreationRequest", new UserCreationRequest());
        return "user/register";
    }

    @GetMapping("/login")
    String login() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
            return "redirect:/";
        }
        return "user/login";
    }

    @PostMapping("/register")
    String register(@Valid UserCreationRequest userCreationRequest, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        log.info("REGISTER TO CREATE USER");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
            return "redirect:/";
        }
        if (bindingResult.hasErrors()) {
            return "user/register";
        }

        try {
            userService.createUser(userCreationRequest);
        } catch (DataIntegrityViolationException e) {
            bindingResult.rejectValue("username", "error.user.duplicate", "Username already exists");
            return "user/register";
        }
        redirectAttributes.addFlashAttribute("message", "Register successfully!");
        return "redirect:/user/login";
    }

    @PostMapping("/create")
    @ResponseBody
    ResponseEntity<String> create(@Valid UserCreationRequest userCreationRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder error = new StringBuilder();
            if (bindingResult.hasFieldErrors("username")) error.append(bindingResult.getFieldError("username").getDefaultMessage()).append("\n");
            if (bindingResult.hasFieldErrors("password")) error.append(bindingResult.getFieldError("password").getDefaultMessage());
            return ResponseEntity.badRequest().body(error.toString());
        }
        try {
            userService.createUser(userCreationRequest);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().body("Username already exists");
        }
        return ResponseEntity.ok().body("User created successfully!");
    }

    @PostMapping("/set-password/{id}")
    @ResponseBody
    ResponseEntity<String> setPassword(@PathVariable Long id,
                                     @Valid SetPasswordRequest request,
                                     BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldError("new_password").getDefaultMessage();
            return ResponseEntity.badRequest().body(errorMessage);
        }
        log.info("SET PASSWORD: {}", request.getNew_password());
        userService.setNewPassword(id, request.getNew_password());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/set-role/{id}")
    @ResponseBody
    ResponseEntity<String> setRole(@PathVariable Long id,
                                       @Valid SetRoleRequest request,
                                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldError("role").getDefaultMessage();
            return ResponseEntity.badRequest().body(errorMessage);
        }
        userService.setNewRole(id, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

}
