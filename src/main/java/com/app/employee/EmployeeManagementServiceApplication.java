package com.app.employee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@EnableAutoConfiguration
@PropertySource("classpath:application-h2.properties")
public class EmployeeManagementServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(com.app.employee.EmployeeManagementServiceApplication.class, args);
    }

}

