package com.app.employee;


import com.app.employee.model.entity.Employee;
import com.app.employee.repository.EmployeeRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = EmployeeManagementServiceApplication.class)
public class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    public void givenInitData_whenApplicationStarts_thenDataIsLoaded() {
        Iterable<Employee> employees = employeeRepository.findAll();

        assertThat(employees)
                .hasSize(5);

    }

    @Test
    public void whenRequestingFirstPageOfSizeTwo_ThenReturnFirstPage() {
        Pageable pageRequest = PageRequest.of(0, 2);

        Page<Employee> result = employeeRepository.findAll(pageRequest);

        assertThat(result.getContent(), hasSize(2));
        assertTrue(result.stream()
                .map(Employee::getId)
                .allMatch(id -> Arrays.asList(1L, 2L)
                        .contains(id)));
    }

}