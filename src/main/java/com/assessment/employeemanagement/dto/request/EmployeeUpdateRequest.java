package com.assessment.employeemanagement.dto.request;

import com.assessment.employeemanagement.enums.Status;
import jakarta.validation.constraints.*;

public record EmployeeUpdateRequest(String name,
                                    @Email(message = "Invalid Email") String email,
                                    String department,
                                    @Pattern(regexp = "^[0-9]{10}$") String phoneNumber,
                                    @Positive(message = "Salary must be greater than zero") Double salary,
                                    @Min(value = 18, message = "Age must be at least 18") Integer age,
                                    Status status) {
}
