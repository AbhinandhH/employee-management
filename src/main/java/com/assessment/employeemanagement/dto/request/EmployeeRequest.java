package com.assessment.employeemanagement.dto.request;

import com.assessment.employeemanagement.enums.Status;
import jakarta.validation.constraints.*;

public record EmployeeRequest(@NotBlank(message = "Name is mandatory") String name,
                              @Email(message = "Invalid Email") @NotBlank(message = "Email is mandatory") String email,
                              @NotBlank(message = "Department is mandatory") String department,
                              @Pattern(regexp = "^[0-9]{10}$") @NotBlank(message = "Phone number is mandatory") String phoneNumber,
                              @NotNull(message = "Salary is mandatory") @Positive(message = "Salary must be greater than zero") Double salary,
                              @Min(value = 18, message = "Age must be at least 18") @NotNull Integer age,
                              Status status) {
    public EmployeeRequest {
        if (status == null) {
            status = Status.ACTIVE;
        }
    }
}
