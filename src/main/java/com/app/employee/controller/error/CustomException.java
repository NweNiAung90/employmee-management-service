package com.app.employee.controller.error;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class CustomException extends RuntimeException {
    private HttpStatus httpStatus;
    private int httpStatusCode;
    private String detail;

    public CustomException(HttpStatus httpStatus, int httpStatusCode, String message, String detail) {
        super(message);
        this.httpStatus = httpStatus;
        this.httpStatusCode = httpStatusCode;
        this.detail = detail;
    }

}
