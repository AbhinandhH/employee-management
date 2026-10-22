package com.assessment.employeemanagement.controller;

import com.assessment.employeemanagement.dto.request.EmployeeRequest;
import com.assessment.employeemanagement.dto.response.EmployeeResponse;
import com.assessment.employeemanagement.enums.Status;
import com.assessment.employeemanagement.service.EmployeeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping("/save")
    @Tag(name = "POST APIs")
    public ResponseEntity<EmployeeResponse> createEmployee(@Valid @RequestBody EmployeeRequest request) {
        EmployeeResponse response = employeeService.createEmployee(request);
        return  ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Tag(name = "GET APIs")
    public ResponseEntity<EmployeeResponse> findEmployeeById(@PathVariable Long id) {
        EmployeeResponse response = employeeService.findEmployeeById(id);
        return  ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/update/{id}")
    @Tag(name = "PUT APIs")
    public ResponseEntity<EmployeeResponse> updateEmployee(@Valid @RequestBody EmployeeRequest request, @PathVariable Long id) {
        EmployeeResponse response = employeeService.updateEmployee(request, id);
        return  ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/list")
    @Tag(name = "GET APIs")
    public ResponseEntity<List<EmployeeResponse>> findAllEmployees() {
        List<EmployeeResponse> response = employeeService.findAllEmployees();
        return  ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/pageablelist")
    @Tag(name = "GET APIs")
    public ResponseEntity<Page<EmployeeResponse>> getAllEmployees(@RequestParam(required = false) String name, @RequestParam(required = false) String department,
                                                                  @RequestParam(required = false) BigDecimal minSalary, @RequestParam(required = false) BigDecimal maxSalary,
                                                                  @RequestParam(required = false) Status status, @PageableDefault(size = 10, sort = "id",direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(employeeService.getAllEmployees(name, department, minSalary, maxSalary, status, pageable));
    }

    @DeleteMapping("/delete/{id}")
    @Tag(name = "DELETE APIs")
    public ResponseEntity<EmployeeResponse> deleteEmployee(@PathVariable Long id) {
        EmployeeResponse response = employeeService.deleteEmployee(id);
        return  ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
