package com.app.employee.service;


import com.app.employee.model.request.EmployeeRequest;
import com.app.employee.model.response.ApiResponse;

import javax.servlet.http.HttpServletRequest;

public interface EmployeeManagementService {

    ApiResponse getAllEmployees(Integer page, Integer size, HttpServletRequest httpServletRequest);

    ApiResponse getEmployeeById(Long id, HttpServletRequest httpServletRequest);

    ApiResponse saveEmployee(EmployeeRequest request, HttpServletRequest httpServletRequest);

    ApiResponse modifyEmployee(EmployeeRequest request, Long id, HttpServletRequest httpServletRequest);

    ApiResponse deleteEmployee(Long id, HttpServletRequest httpServletRequest);
}
