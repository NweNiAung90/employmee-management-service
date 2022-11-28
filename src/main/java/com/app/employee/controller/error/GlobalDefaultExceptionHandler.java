package com.app.employee.controller.error;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalDefaultExceptionHandler {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponseTemplate> handleCustomException(
            CustomException ex) {

        ErrorResponseTemplate errorResponseTemplate = new ErrorResponseTemplate();

        errorResponseTemplate.setHttpStatusCode(ex.getHttpStatusCode());
        errorResponseTemplate.setHttpStatus(ex.getHttpStatus());
        errorResponseTemplate.setMessage(ex.getMessage());
        errorResponseTemplate.setDetail(ex.getDetail());

        return new ResponseEntity<ErrorResponseTemplate>(errorResponseTemplate, ex.getHttpStatus());
    }
}
