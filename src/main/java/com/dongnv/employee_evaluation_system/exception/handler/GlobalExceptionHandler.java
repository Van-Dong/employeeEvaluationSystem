package com.dongnv.employee_evaluation_system.exception.handler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler
    public String handlingException(RuntimeException exception) {
        exception.printStackTrace();
        return "index";
    }
}
