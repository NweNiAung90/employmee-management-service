package com.app.employee.model.common;

import lombok.Data;

@Data
public class AuthErrorResponse {
    private String error;

    public AuthErrorResponse(String error) {
        super();
        this.error = error;
    }

    public AuthErrorResponse() {
        super();
    }
}
