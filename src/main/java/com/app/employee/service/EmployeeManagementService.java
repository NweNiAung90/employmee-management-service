package com.app.employee.service;


import com.app.employee.model.request.EmployeeRequest;
import com.app.employee.model.response.ApiResponse;

public interface EmployeeManagementService {

    ApiResponse getAllEmployees(Integer page, Integer size);

    ApiResponse getEmployeeById(Long id);

    ApiResponse saveEmployee(EmployeeRequest request);

    ApiResponse modifyEmployee(EmployeeRequest request, Long id);

    ApiResponse deleteEmployee(Long id);
}
