package com.app.employee;

import com.app.employee.model.entity.Employee;
import com.app.employee.model.response.ApiResponse;
import com.app.employee.repository.EmployeeRepository;
import com.app.employee.service.EmployeeManagementService;
import com.app.employee.service.impl.EmployeeManagementServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class EmployeeServiceIntegrationTest {
    @TestConfiguration
    static class EmployeeManagementServiceIntegrationTestConfiguration {
        @Bean
        public EmployeeManagementService employeeManagementService() {
            return new EmployeeManagementServiceImpl();
        }
    }

    @Autowired
    EmployeeManagementService employeeManagementService;

    @MockBean
    EmployeeRepository employeeRepository;

    @Before
    public void setUp() {
        Employee employee = new Employee(1L, "William", "Smith", "6 - Chief Information Technology Officer", "CEO", "IT", "male", "1 January 1963");

        List<Employee> allEmployees = Arrays.asList(employee);
        Mockito.when(employeeRepository.findById(employee.getId())).thenReturn(Optional.of(employee));
    }

    @Test
    public void whenValidId_thenEmployeeShouldBeFound() {

        ApiResponse apiResponse = employeeManagementService.getEmployeeById(1L);
        assertThat(apiResponse.getEmployeeResponse().getFirstName()).isEqualTo("William");

        verifyFindByIdIsCalledOnce();
    }

    private void verifyFindByIdIsCalledOnce() {
        Mockito.verify(employeeRepository, VerificationModeFactory.times(1)).findById(Mockito.anyLong());
        Mockito.reset(employeeRepository);
    }
}
