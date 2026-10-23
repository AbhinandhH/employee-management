package com.assessment.employeemanagement.dto.response;

import com.assessment.employeemanagement.enums.Status;

public record EmployeeResponse(Long id,
                               String name,
                               String email,
                               Integer age,
                               Status status,
                               String department,
                               Double salary,
                               String phoneNumber) {
}
