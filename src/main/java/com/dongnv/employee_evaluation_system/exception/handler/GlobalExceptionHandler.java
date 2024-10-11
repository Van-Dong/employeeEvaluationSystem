package com.dongnv.employee_evaluation_system.exception.handler;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.dongnv.employee_evaluation_system.exception.AppException;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(value = RuntimeException.class)
    public String handlingException(RuntimeException exception, Model model) {
        log.info("Exception: ", exception);
        model.addAttribute("errorMessage", exception.getMessage());
        return "error/400";
    }

    @ExceptionHandler(value = AppException.class)
    public String handlingAppException(AppException exception, Model model) {
        log.info("Exception: ", exception);
        model.addAttribute("errorMessage", exception.getErrorCode().getMessage());
        return "error/400";
    }
}
