package com.app.employee.controller.error;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ErrorResponseTemplate {
    @JsonIgnore
    private Integer httpCode;
    private int httpStatusCode;
    private HttpStatus httpStatus;
    private String message;
    private String detail;
}
