package com.app.employee.model.response;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class EmployeeListResponse {
    private List<EmployeeResponse> employeeResponseList = new ArrayList<>();

    private Integer page;

    private Integer size;

    private Long totalElements;

    private Integer totalPages;

    private Boolean isLast;
}
