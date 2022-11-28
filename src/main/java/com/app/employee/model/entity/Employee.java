package com.app.employee.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Table(name = "employee")
@Entity
@Getter
@Data
@AllArgsConstructor
public class Employee {
    private @Id
    @GeneratedValue
    @Column(name = "ID")
    Long id;

    @Column(name = "FIRST_NAME", nullable = false)
    private String firstName;

    @Column(name = "LAST_NAME", nullable = false)
    private String lastName;

    private String title;

    @Column(name = "EXTERNAL_TITLE", nullable = false)
    private String externalTitle;

    private String department;

    private String gender;

    @Column(name = "DATE_OF_BIRTH", nullable = false)
    private String dateOfBirth;

    public Employee() {
    }
}
