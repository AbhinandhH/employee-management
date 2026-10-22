package com.assessment.employeemanagement.service;

import com.assessment.employeemanagement.dto.request.EmployeeRequest;
import com.assessment.employeemanagement.dto.response.EmployeeResponse;
import com.assessment.employeemanagement.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface EmployeeService {
    EmployeeResponse createEmployee(EmployeeRequest request);
    EmployeeResponse findEmployeeById(Long employeeId);
    List<EmployeeResponse> findAllEmployees();
    EmployeeResponse updateEmployee(EmployeeRequest request, Long employeeId);
    EmployeeResponse deleteEmployee(Long employeeId);
    Page<EmployeeResponse> getAllEmployees(String name, String department, BigDecimal minSalary, BigDecimal maxSalary, Status status, Pageable pageable);
}
