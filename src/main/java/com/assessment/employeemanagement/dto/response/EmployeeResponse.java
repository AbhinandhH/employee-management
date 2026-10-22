package com.assessment.employeemanagement.dto.response;

public record EmployeeResponse(Long id,
                               String name,
                               String email,
                               String department,
                               Double salary,
                               String phoneNumber) {
}
