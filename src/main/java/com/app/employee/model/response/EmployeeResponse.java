package com.app.employee.model.response;

import lombok.Data;

@Data
public class EmployeeResponse {
    Long id;

    private String firstName;

    private String lastName;

    private String title;

    private String externalTitle;

    private String department;

    private String gender;

    private String dateOfBirth;
}
