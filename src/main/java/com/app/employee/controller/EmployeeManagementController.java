package com.app.employee.controller;

import com.app.employee.controller.error.CustomException;
import com.app.employee.model.request.EmployeeRequest;
import com.app.employee.model.response.ApiResponse;
import com.app.employee.service.EmployeeManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.app.employee.constant.Enum.DB_EXCEPTION;

@RestController()
@RequestMapping(value = "/api/v1")
@RequiredArgsConstructor
public class EmployeeManagementController {
    private static final Logger logger = LogManager.getLogger(EmployeeManagementController.class);

    @Autowired
    private EmployeeManagementService employeeManagementService;

    @Operation(summary = "Retrieving all employee")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class))}),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content)})
    @GetMapping("/employees")
    public ResponseEntity<ApiResponse> getAllEmployees(@RequestParam(defaultValue = "0") Integer page,
                                                       @RequestParam(defaultValue = "10") Integer size) {
        logger.info("Incoming Get All Employees method in REST EmployeeManagementController with page : " + page + " and size " + size);

        try {
            ApiResponse apiResponse = employeeManagementService.getAllEmployees(page, size);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);

        } catch (Exception e) {

            logger.error("Exception in EmployeeManagementController getAllEmployees() - Error Message: " + e.getMessage(), e);
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, 500, e.getMessage(), e.getMessage());

        }
    }

    @Operation(summary = "retrieving one employee by ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class))}),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content)})
    @GetMapping("/employees/{employeeId}")
    public ResponseEntity<ApiResponse> getEmployee(@PathVariable(value = "employeeId") Long employeeId) {
        logger.info("Incoming Get Employee method in REST EmployeeManagementController with employee Id : " + employeeId);

        try {

            ApiResponse apiResponse = employeeManagementService.getEmployeeById(employeeId);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);

        } catch (Exception e) {

            logger.error("Exception in EmployeeManagementController getEmployee() - Error Message: " + e.getMessage(), e);
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, 500, e.getMessage(), e.getMessage());

        }
    }

    @Operation(summary = "saving one employee")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class))}),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content)})
    @PostMapping("/employees")
    public ResponseEntity<ApiResponse> saveEmployee(@Valid @RequestBody EmployeeRequest request) {
        logger.info("Incoming Save Employee method in REST EmployeeManagementController with Employee Request: " + request);

        try {

            ApiResponse apiResponse = employeeManagementService.saveEmployee(request);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);

        } catch (Exception e) {

            logger.error("Exception in EmployeeManagementController saveEmployee() - Error Message: " + e.getMessage(), e);
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, 500, e.getMessage(), e.getMessage());

        }
    }

    @Operation(summary = "modifying one employee")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class))}),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content)})
    @PutMapping("/employees/{employeeId}")
    public ResponseEntity<ApiResponse> modifyEmployee(@Valid @RequestBody EmployeeRequest request, @PathVariable(value = "employeeId") Long employeeId) {
        logger.info("Incoming Save Employee method in REST EmployeeManagementController with Employee Request: " + request);

        try {

            ApiResponse apiResponse = employeeManagementService.modifyEmployee(request, employeeId);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);

        } catch (Exception e) {

            logger.error("Exception in EmployeeManagementController saveEmployee() - Error Message: " + e.getMessage(), e);
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, 500, e.getMessage(), e.getMessage());

        }
    }

    @Operation(summary = "deleting one employee by ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class))}),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content)})
    @DeleteMapping("/employees/{employeeId}")
    public ResponseEntity<ApiResponse> deleteEmployee(@PathVariable(value = "employeeId") Long employeeId) {
        logger.info("Incoming Save Employee method in REST EmployeeManagementController with Employee Request: " + employeeId);

        try {

            ApiResponse apiResponse = employeeManagementService.deleteEmployee(employeeId);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);

        } catch (Exception e) {

            logger.error("Exception in EmployeeManagementController saveEmployee() - Error Message: " + e.getMessage(), e);
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, 500, e.getMessage(), e.getMessage());

        }
    }

}
