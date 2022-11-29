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
        String token = "Bearer eyJraWQiOiJNYmV1VmVVWlhVT2FJcDgwYmx1XC9sanFOQjNKZE9aSDgxQ3JGU0tpMmVcL2M9IiwiY3R5IjoiSldUIiwiZW5jIjoiQTEyOENCQy1IUzI1NiIsImFsZyI6ImRpciJ9..XAWO8KJvONIOpqpDXG-7-g.P-kDeUY2ecupPHz0bXHnLL1fPsVXkelHHuqPjN1YALXBlGBsFrUE5AJTM2Krl417X_69_0GiA3LYp9cBb7XJ1EJ8weVoWjIZc1MkElxu5aufE5JTWBpjvNSq5FpZo4W3Vub97SCu2y6SCrgFSxHy0C9brOvFAQSwgaA1n0VaOOFVjxSf5X0aAJyi1oECPyaB6hbUXsZQjnJbFTKrOn5D9w3VahRqQv7OeB0HDBOEyEhyweqvwKKTRUbmNl9jjwa6CYYUxX1xulBpbJe1XDmFhO4OBR7j-EtYJu2GMONZrgCHouxZjvy93gvSuB1cYLgYs1m-I5helPI7CryIDZJ9Ukt_kaQrSoDJG_RR5ySjnLmUV9nIDBUhNP3YI0VkaCrWIHxx-hkpFiTp91KFTJRV6bFrA9rETXre3CcEhcO4gO3FBprwr1oT_mHNl4XyeT66AzwKvu2PU8C5f9PQRDlrcS9L-mfS0TT0Bueo7ABz92CYmyKvmRyEI8mHROq9HdPRoNyIeNAWmI9kxEq7KfgEVEUHdKYuiQumGlaj3tc3o5Kfu4Zc7yrUlp-S2r-xqlZuYXqd7DQLbyBb35SU7bMQhry7SvqkqbMaW4-zfmJiS-q39ZyZ-KZRE7t5TsHh6FyVwvWG6nx1hWM9D_D-SUJTeuuk9FJyUHbEFUlsojRF6U3o2bNXW_HOfYYWr70MO8wfCC89NccOH96-ig4G57L3jDVVKFmhayLBIrouT0Bce5BYj3cD9yeRcIkd5zPRpsO-wbOWd4kesldJWo0AhfIUn7HWUMLzRtct1r-pzB9dWlq3xJ38n0pFZRO2QwbN5HRZ0V_zpea8hPLvvPI0d_z4FdMc7kOjifufoukIky80V-5pKliPc5wph7ir_cYoJzBDnqI4I0inbmCTNbI_cMZAF8mfSEIBuEcKoMOdyLAC0O8W5iRE7mkyA6f7ju1tDVGioUTsrBRkUv4ww8p26LoWMqTMTx6rMGndD9-8zcVvH8Ar49I0wG0lwxx3kuZrJhHwbwfYFdoSPHjBHF1ScQLfXc_yCq5yiDc-gh0aDdMdE1Wl2o9XolE7O55lqvAF07pg-blsX_2cXhAt_U195oHsJxTlomNVNciWUlYOzQa9JciqJEEV08kY18qUfrI-3BDDpu9bM5QmXn_A39Ck9ZYKLVcr0WUUnHJltwPGni8HrM7AwDG4Q57sSXzSmAhPuyZp4DqBfCLwqRKAoKyXkKkUu7Z3kYEGI6RL_iT4kPmovhSPY1nLxbblTcmTGyEGDemGmlicRWh4DaRrEpXmVe2uPPbp3HJh4pUUMHypxh0-tdmpxw1UHe2YGZVA2UJvZpcrkarF-WQbOyHWmZyOXohYe-YvMPRlw3rrB2VKvx2M6Zw1CryHZMbItiY8ceN8BRoaGebnKuTlYslNVUPNW3qnJcKcXqHBm2QZX--X7QVcGNDeWlenR9YJ5UgKxcgzhPA_JJAFSpeS7PfYiJNEpLO_fNkHjLwJ4K2yT7VCGqWokbk.m0vbzKr-Tt_5KSEFBNUS7w";

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(HttpHeaders.AUTHORIZATION, token);
        ApiResponse apiResponse = employeeManagementService.getEmployeeById(1L, request);
        assertThat(apiResponse.getEmployeeResponse().getFirstName()).isEqualTo("William");

        verifyFindByIdIsCalledOnce();
    }

    private void verifyFindByIdIsCalledOnce() {
        Mockito.verify(employeeRepository, VerificationModeFactory.times(1)).findById(Mockito.anyLong());
        Mockito.reset(employeeRepository);
    }
}
