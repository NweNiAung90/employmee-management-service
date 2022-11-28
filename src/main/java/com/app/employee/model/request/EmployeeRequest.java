package com.app.employee.model.request;

import lombok.Data;

@Data
public class EmployeeRequest {
    private String firstName;

    private String lastName;

    private String title;

    private String externalTitle;

    private String department;

    private String gender;

    private String dateOfBirth;
}
