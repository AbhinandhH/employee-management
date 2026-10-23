package com.assessment.employeemanagement.controller;


import com.assessment.employeemanagement.dto.response.EmployeeResponse;
import com.assessment.employeemanagement.enums.Status;
import com.assessment.employeemanagement.exception.EmployeeNotFoundException;
import com.assessment.employeemanagement.service.EmployeeService;
import com.assessment.employeemanagement.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.http.MediaType;

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EmployeeServiceImpl employeeService;

    @Test
    void createEmployee_withInvalidEmail_shouldReturnBadRequest() throws Exception {

        mockMvc.perform(post("/api/employee/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                        "name": "Abhinandh",
                        "email": "invalid-email",
                        "department": "IT",
                        "salary": 30000.0,
                        "age": 26,
                        "status": "ACTIVE"
                    }
                    """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getEmployeeById_existingId_shouldReturnEmployee() throws Exception {

        EmployeeResponse response = new EmployeeResponse(
                1L,
                "Abhinandh",
                "abhinandh@example.com",
                30,
                Status.ACTIVE,
                "IT",
                50000.00,
                "9887564333"
        );

        when(employeeService.findEmployeeById(1L))
                .thenReturn(response);

        mockMvc.perform(get("/api/employee/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Abhinandh"))
                .andExpect(jsonPath("$.email").value("abhinandh@example.com"));
    }

    @Test
    void getEmployeeById_missingId_shouldReturn404() throws Exception {
        when(employeeService.findEmployeeById(100L)).thenThrow(new EmployeeNotFoundException("Employee not found with id: 100"));
        mockMvc.perform(get("/api/employee/{id}", 100L)).andExpect(status().isNotFound());
    }
}
