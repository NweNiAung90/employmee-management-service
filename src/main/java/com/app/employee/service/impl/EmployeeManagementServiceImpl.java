package com.app.employee.service.impl;

import com.app.employee.controller.error.CustomException;
import com.app.employee.model.entity.Employee;
import com.app.employee.model.request.EmployeeRequest;
import com.app.employee.model.response.ApiResponse;
import com.app.employee.model.response.EmployeeListResponse;
import com.app.employee.model.response.EmployeeResponse;
import com.app.employee.repository.EmployeeRepository;
import com.app.employee.service.CommonService;
import com.app.employee.service.EmployeeManagementService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.app.employee.constant.Enum.*;

@Service
public class EmployeeManagementServiceImpl implements EmployeeManagementService {
    private static final Logger logger = LogManager.getLogger(EmployeeManagementServiceImpl.class);

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    private CommonService commonService;

    @Override
    public ApiResponse getAllEmployees(Integer page, Integer size, HttpServletRequest httpServletRequest) {
        Optional<String> existingUser = commonService.getCurrentUser(httpServletRequest);
        logger.debug("Existing User " + existingUser);

        if (!existingUser.isPresent()) {
            throw new CustomException(HttpStatus.BAD_REQUEST, 400, ACCESS_DENIED, ACCESS_DENIED);
        } else {
            Pageable pageable = PageRequest.of(page, size);
            Page<Employee> employees = employeeRepository.findAll(pageable);
            logger.info("Employee Size : " + employees.getTotalElements());

            if (employees.isEmpty()) {
                logger.error("No fees(records) found in database.");
                throw new RuntimeException(ERROR_DETAILS_NO_EMPLOYEE_LIST);
            }

            try {
                EmployeeListResponse employeeListResponse = new EmployeeListResponse();
                List<EmployeeResponse> employeeList = new ArrayList<>();
                for (Employee e : employees) {
                    EmployeeResponse employeeResponse = new EmployeeResponse();
                    employeeResponse.setId(e.getId());
                    employeeResponse.setFirstName(e.getFirstName());
                    employeeResponse.setLastName(e.getLastName());
                    employeeResponse.setTitle(e.getTitle());
                    employeeResponse.setExternalTitle(e.getExternalTitle());
                    employeeResponse.setDepartment(e.getDepartment());
                    employeeResponse.setGender(e.getGender());
                    employeeResponse.setDateOfBirth(e.getDateOfBirth());
                    employeeList.add(employeeResponse);
                }

                logger.info("Employee List : " + employeeList);
                employeeListResponse.setEmployeeResponseList(employeeList);
                employeeListResponse.setPage(employees.getPageable().getPageNumber());
                employeeListResponse.setSize(employees.getSize());
                employeeListResponse.setTotalElements(employees.getTotalElements());
                employeeListResponse.setTotalPages(employees.getTotalPages());
                employeeListResponse.setIsLast(employees.isLast());
                ApiResponse apiResponse = new ApiResponse(HttpStatus.OK.value(), HttpStatus.OK, SUCCESS, employeeListResponse);
                logger.info("Api Response : " + apiResponse);
                return apiResponse;

            } catch (Exception e) {
                logger.error("Exception while retrieving Employee List Employee DB: " + e);
                throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, 500, DB_EXCEPTION, e.getMessage());
            }
        }

    }

    @Override
    public ApiResponse getEmployeeById(Long id, HttpServletRequest httpServletRequest) {
        Optional<String> existingUser = commonService.getCurrentUser(httpServletRequest);
        logger.debug("Existing User " + existingUser);

        if (!existingUser.isPresent()) {
            throw new CustomException(HttpStatus.BAD_REQUEST, 400, ACCESS_DENIED, ACCESS_DENIED);
        } else {
            if (id == null) {
                logger.error("Invalid/Incomplete request parameter(s) provided!");
                throw new CustomException(HttpStatus.BAD_REQUEST, 400, BAD_REQUEST, ADD_EMPLOYEE_ID);
            }

            try {
                Optional<Employee> employee = employeeRepository.findById(id);
                ApiResponse apiResponse;
                if (!employee.isEmpty()) {
                    apiResponse = new ApiResponse(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND, EMPLOYEE_NOT_FOUND);
                }
                EmployeeResponse employeeResponse = new EmployeeResponse();
                employeeResponse.setId(employee.get().getId());
                employeeResponse.setFirstName(employee.get().getFirstName());
                employeeResponse.setLastName(employee.get().getLastName());
                employeeResponse.setTitle(employee.get().getTitle());
                employeeResponse.setExternalTitle(employee.get().getExternalTitle());
                employeeResponse.setDepartment(employee.get().getDepartment());
                employeeResponse.setGender(employee.get().getGender());
                employeeResponse.setDateOfBirth(employee.get().getDateOfBirth());

                logger.info("Employee : " + employeeResponse + " with Id : " + id);
                apiResponse = new ApiResponse(HttpStatus.OK.value(), HttpStatus.OK, SUCCESS, employeeResponse);
                return apiResponse;

            } catch (Exception e) {
                logger.error("Exception while retrieving Employee Employee DB: " + e);
                throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, 500, DB_EXCEPTION, e.getMessage());
            }
        }

    }

    @Override
    public ApiResponse saveEmployee(EmployeeRequest request, HttpServletRequest httpServletRequest) {
        Optional<String> existingUser = commonService.getCurrentUser(httpServletRequest);
        logger.debug("Existing User " + existingUser);

        if (!existingUser.isPresent()) {
            throw new CustomException(HttpStatus.BAD_REQUEST, 400, ACCESS_DENIED, ACCESS_DENIED);
        } else {
            if (request.getFirstName().isEmpty() || request.getLastName().isEmpty() || request.getTitle().isEmpty() || request.getExternalTitle().isEmpty()
                    || request.getDepartment().isEmpty() || request.getGender().isEmpty() || request.getDateOfBirth().isEmpty()) {
                logger.error("Invalid/Incomplete request parameter(s) provided!");
                throw new CustomException(HttpStatus.BAD_REQUEST, 400, BAD_REQUEST, ADD_EMPLOYEE_DATA);
            }

            try {
                ModelMapper modelMapper = new ModelMapper();
                Employee employee = modelMapper.map(request, Employee.class);

                logger.info("Employee Entity : " + employee);
                employeeRepository.save(employee);
                return new ApiResponse(HttpStatus.OK.value(), HttpStatus.OK, SUCCESS);

            } catch (Exception e) {
                logger.error("Exception while saving Employee DB: " + e);
                throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, 500, DB_EXCEPTION, e.getMessage());
            }
        }

    }

    @Override
    public ApiResponse modifyEmployee(EmployeeRequest request, Long id, HttpServletRequest httpServletRequest) {
        Optional<String> existingUser = commonService.getCurrentUser(httpServletRequest);
        logger.debug("Existing User " + existingUser);

        if (!existingUser.isPresent()) {
            throw new CustomException(HttpStatus.BAD_REQUEST, 400, ACCESS_DENIED, ACCESS_DENIED);
        } else {
            if (request.getFirstName().isEmpty() || request.getLastName().isEmpty() || request.getTitle().isEmpty() || request.getExternalTitle().isEmpty()
                    || request.getDepartment().isEmpty() || request.getGender().isEmpty() || request.getDateOfBirth().isEmpty()) {
                logger.error("Invalid/Incomplete request parameter(s) provided!");
                throw new CustomException(HttpStatus.BAD_REQUEST, 400, BAD_REQUEST, ADD_EMPLOYEE_DATA);
            }

            ModelMapper modelMapper = new ModelMapper();
            Employee updatedEmployee = modelMapper.map(request, Employee.class);

            logger.info("Employee Entity : " + updatedEmployee);
            try {
                Optional<Employee> employeeOptional = employeeRepository.findById(id)
                        .map(employee -> {
                            employee.setFirstName(updatedEmployee.getFirstName());
                            employee.setLastName(updatedEmployee.getLastName());
                            employee.setTitle(updatedEmployee.getTitle());
                            employee.setExternalTitle(updatedEmployee.getExternalTitle());
                            employee.setDepartment(updatedEmployee.getDepartment());
                            employee.setGender(updatedEmployee.getGender());
                            employee.setDateOfBirth(updatedEmployee.getDateOfBirth());
                            return employeeRepository.save(employee);
                        });

                ApiResponse apiResponse;
                if (employeeOptional.isPresent()) {
                    apiResponse = new ApiResponse(HttpStatus.OK.value(), HttpStatus.OK, SUCCESS);
                } else {
                    apiResponse = new ApiResponse(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND, EMPLOYEE_NOT_FOUND);
                }
                return apiResponse;

            } catch (Exception e) {
                logger.error("Exception while updating Employee DB: " + e);
                throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, 500, DB_EXCEPTION, e.getMessage());
            }
        }
    }

    @Override
    public ApiResponse deleteEmployee(Long id, HttpServletRequest httpServletRequest) {
        Optional<String> existingUser = commonService.getCurrentUser(httpServletRequest);
        logger.debug("Existing User " + existingUser);

        if (!existingUser.isPresent()) {
            throw new CustomException(HttpStatus.BAD_REQUEST, 400, ACCESS_DENIED, ACCESS_DENIED);
        } else {
            if (id == null) {
                logger.error("Invalid/Incomplete request parameter(s) provided!");
                throw new CustomException(HttpStatus.BAD_REQUEST, 400, BAD_REQUEST, ADD_EMPLOYEE_ID);
            }
            try {
                employeeRepository.deleteById(id);

                return new ApiResponse(HttpStatus.OK.value(), HttpStatus.OK, SUCCESS);

            } catch (Exception e) {
                logger.error("Exception while deleting Employee DB: " + e);
                throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, 500, DB_EXCEPTION, e.getMessage());
            }
        }
    }

}
