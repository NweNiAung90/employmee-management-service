package com.app.employee;

import com.app.employee.model.response.ApiResponse;
import com.app.employee.model.response.EmployeeListResponse;
import com.app.employee.model.response.EmployeeResponse;
import com.app.employee.service.EmployeeManagementService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;

import static com.app.employee.constant.Enum.SUCCESS;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mvc;

    @MockBean
    EmployeeManagementService employeeManagementService;

    String token = "Bearer eyJraWQiOiJNYmV1VmVVWlhVT2FJcDgwYmx1XC9sanFOQjNKZE9aSDgxQ3JGU0tpMmVcL2M9IiwiY3R5IjoiSldUIiwiZW5jIjoiQTEyOENCQy1IUzI1NiIsImFsZyI6ImRpciJ9..fl_4obxb8LlDi3ovH7B-bQ.0tEkk3Kvw5b6MUldbzvanKn4yNdbaWZprlVx6VnknxYrXyuHZLGVnjRXhQ02czX2ImF74bUn8jJ_fQmLVxXqpHcP_PXdEwugQfeLFarQZuunyEWW668dDl8pMlDLF7-4OMiNORXwhwmeT10U46KMofWgN1Y7D-grNHJyoeuuqsbRTJ5Ahilr9b4cCnvBeDbiLUdwPSZxaO9r5NZyC7BMtEQkHIlT9i2LXOJ-1YAQeYUkLlFO4YfVpiuerDsqJHU1wmm1zZ6aYdZKmZD42IsOQwRmvAUfj1csnONCiMOEvigKZo5HEMBKvMGZXq2DbMFT0gT6Gy2Oh-Jm2FP3aKq5b1NoCz8DSFpYOO7iNFNgdyQDJucMR2oZbIFXpRF5w7DjH9VvRJbnlrTL9VYDylbTva1fUz7Di_ptahBdweauRwypuQhNz1WUqvy4wC931_mnFHVZdfXFVuBZN62O1PjmBTxh4Q458Iu4PmYig19M39quEeAfrjcGrnHBBbW88Yu3xrm6hcmIR1baSy3mhTtl2lTWb_rFNGXenOhm5hSkIkIq6vFOC-YsN6nxbgY8dvX58o5OQia1xKn6gdHDs5qoQrIPweTMyeCrag9Uf6YGt4pdLvWoNdn0crYxNoJnh0inY_GvvjlSWdui7zy5WwQG6SNZl1iu6sZOVOMYTByJfyhlAg3H15QTDmPbIz1jIHdhLPZNQq7x1pXQSbnUuLh-H9PyAyTbrSzovea0hETTekqnkWzC5BvgZ36avrXCXyhmeBoZbcrhcaoPf8dGcp73iEaJ9Hi4JXPE4vD0ownDlGvOITaM_16VZ7hjBZcZlad0S87mkTpegboOnXZ8tPHJokcxI7j6k1vWFrLeBi1HXgdZWMJp7mJclOB-tDjnSbI0cvKH1AKEE3Xv8dzEQF7_OX8AqTDS-dYaY5xMMNxZfi6Pafd7ItuOa5Ej2Als0Nb9_rH5KDiWbCQ-NJT5IZmL60UDKZz4yrrLHY9qcMoPhWCUaJdFt9aYaIyGTHxUOfoOflGOsv27MgbfrELG-E0_iYz-bk_az2Xci4qyIGTodGPeOaVWIKw2WpKfEdOec1iwZzC4ohDKVUow_8a-D-JJBMYbp-_jBzF60VPysusHES1HjVW1qucuMv8GWDITG4K3-uj159uhg-_QRKgIQnQXFb_CZyAh5NrXJrQ8aibTAwAqNG33_3xovSzg8WN6jCDcwQZadH6QxWUqZgHTUehHgpyR-ZHSFOfx43rTHopy1N57bsxPRlaATH1Jg7zmkMNKSVxfbuRJf_Al2CIjD-XPLwOo92XrF_tAqrBxyEf5qRaoSkSv51osd0GuPnDgLRDXAMcXNLwQBAgYZ63iwxzCsMjYK24bZMq60pHKmKhwh05cKgxrdjRrXc0IIIvcMcMIq8Dw8SjO_VV2SMKS0lieljc8QCpWDRNoVRV53QxMDXnrhhhQKm9oS4JeomRgRlquwEjzhKwBWTR609zzma3pp1qw8Ntj22WFrZmXbHFWZ7g.B4-l1nsXKwHUFmKcqfwiHQ";
    TestRestTemplate restTemplate = new TestRestTemplate();
    HttpHeaders headers = new HttpHeaders();

    @Before
    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @WithMockUser(username = "stitch")
    public void testRetrieveAllEmployee() throws Exception {

        this.mvc.perform(get("/api/v1/employees?page=0&size=3")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer token")
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk());
        //.andExpect(content().string(containsString("William")));
    }

    @Test
    @WithMockUser(username = "stitch")
    public void givenEmployees_whenGetEmployees_thenReturnJsonArray() throws Exception {

        Integer page = 0;
        Integer size = 1;

        EmployeeResponse employee = new EmployeeResponse();
        employee.setId(1L);
        employee.setFirstName("William");
        employee.setLastName("Smith");
        employee.setTitle("6 - Chief Information Technology Officer");
        employee.setExternalTitle("CEO");
        employee.setGender("male");
        employee.setDepartment("IT");
        employee.setDateOfBirth("1 January 1963");

        List<EmployeeResponse> allEmployees = Arrays.asList(employee);
        ModelMapper modelMapper = new ModelMapper();
        EmployeeListResponse employeeListResponse = modelMapper.map(employee, EmployeeListResponse.class);
        employeeListResponse.setPage(page);
        employeeListResponse.setSize(size);
        employeeListResponse.setTotalElements(5L);
        employeeListResponse.setTotalPages(5);
        employeeListResponse.setIsLast(false);

        ApiResponse apiResponse = new ApiResponse(HttpStatus.OK.value(), HttpStatus.OK, SUCCESS, employeeListResponse);
        System.out.println("Api Response : " + apiResponse);
        given(employeeManagementService.getAllEmployees(page, size)).willReturn(apiResponse);

        this.mvc.perform(get("/api/v1/employees?page=0&size=1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer token"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "stitch")
    public void testRetrieveEmployee() throws Exception {
        HttpHeaders headers = getHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer token");
        HttpEntity<String> jwtEntity = new HttpEntity<String>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/employee-management/api/v1/employees/1", HttpMethod.GET, jwtEntity, String.class);

        String expected = "{\"httpStatusCode\":200,\"httpStatus\":\"OK\",\"message\":\"success\",\"employeeResponseList\":null,\"employeeResponse\":{\"id\":1,\"firstName\":\"William\",\"lastName\":\"Smith\",\"title\":\"6 - Chief Information Technology Officer\",\"externalTitle\":\"CEO\",\"department\":\"IT\",\"gender\":\"male\",\"dateOfBirth\":\"1 January 1963\"}}";

        JSONAssert.assertEquals(expected, response.getBody(), false);
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        return headers;
    }
}
