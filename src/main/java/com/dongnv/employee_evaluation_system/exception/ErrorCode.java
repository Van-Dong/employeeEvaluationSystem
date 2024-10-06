package com.dongnv.employee_evaluation_system.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    DEPARTMENT_NOT_FOUND(1001, "Department Not Found", HttpStatus.BAD_REQUEST),
    EMPLOYEE_NOT_FOUND(1002, "Employee Not Found", HttpStatus.BAD_REQUEST),
    ERROR_WHEN_STORE_IMAGE(1003, "An Error Occurred when try store image file", HttpStatus.INTERNAL_SERVER_ERROR),

    ;
    ErrorCode(int code, String message, HttpStatusCode httpStatusCode) {
        this.code =  code;
        this.message = message;
        this.httpStatusCode = httpStatusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatusCode httpStatusCode;
}
