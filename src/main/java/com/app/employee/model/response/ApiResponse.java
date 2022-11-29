package com.app.employee.model.response;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ApiResponse {

    private int httpStatusCode;
    private HttpStatus httpStatus;
    private String message;
    private EmployeeListResponse employeeResponseList;
    private EmployeeResponse employeeResponse;

    public ApiResponse(int httpStatusCode, HttpStatus httpStatus, String message, EmployeeListResponse employeeResponseList) {
        this.httpStatusCode = httpStatusCode;
        this.httpStatus = httpStatus;
        this.message = message;
        this.employeeResponseList = employeeResponseList;
    }

    public ApiResponse(int httpStatusCode, HttpStatus httpStatus, String message, EmployeeResponse employeeResponse) {
        this.httpStatusCode = httpStatusCode;
        this.httpStatus = httpStatus;
        this.message = message;
        this.employeeResponse = employeeResponse;
    }

    public ApiResponse(int httpStatusCode, HttpStatus httpStatus, String message) {
        this.httpStatusCode = httpStatusCode;
        this.httpStatus = httpStatus;
        this.message = message;
    }
}