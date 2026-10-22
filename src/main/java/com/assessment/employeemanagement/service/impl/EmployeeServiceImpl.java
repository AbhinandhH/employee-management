package com.assessment.employeemanagement.service.impl;

import com.assessment.employeemanagement.dto.request.EmployeeRequest;
import com.assessment.employeemanagement.dto.response.EmployeeResponse;
import com.assessment.employeemanagement.entity.Employee;
import com.assessment.employeemanagement.entity.EmployeeAudit;
import com.assessment.employeemanagement.enums.AuditAction;
import com.assessment.employeemanagement.enums.Status;
import com.assessment.employeemanagement.exception.EmailExistsException;
import com.assessment.employeemanagement.exception.EmployeeNotFoundException;
import com.assessment.employeemanagement.mapper.EmployeeMapper;
import com.assessment.employeemanagement.repository.EmployeeAuditRepository;
import com.assessment.employeemanagement.repository.EmployeeRepository;
import com.assessment.employeemanagement.service.EmployeeService;
import com.assessment.employeemanagement.specification.EmployeeSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final TransactionTemplate transactionTemplate;
    private final EmployeeAuditRepository employeeAuditRepository;

    @Override
    public EmployeeResponse createEmployee(EmployeeRequest request) {
        Employee.EmployeeBuilder  employeeBuilder = Employee.builder();
        if(employeeRepository.existsByEmail(request.email())) {
            log.info("Employee with email {} already exists", request.email());
            throw new EmailExistsException("Email already exists '" +  request.email() + "'");
        } else if(employeeRepository.existsByPhoneNumber(request.phoneNumber())) {
            log.info("Employee with phoneNumber {} already exists", request.email());
            throw new EmailExistsException("Phone Number already exists '" +  request.phoneNumber() + "'");
        }
        Employee employeeToBeSaved = employeeBuilder.name(request.name())
                .email(request.email())
                .phoneNumber(request.phoneNumber())
                .department(request.department())
                .salary(new BigDecimal(request.salary()))
                .age(request.age())
                .status(Status.ACTIVE)
                .build();

        return transactionTemplate.execute(status -> {
            Employee savedEmployee = employeeRepository.save(employeeToBeSaved);

            if(log.isDebugEnabled()) {
                log.debug("Employee with id {} saved successfully", employeeToBeSaved.getId());
            }

            EmployeeAudit audit = new EmployeeAudit();
            audit.setEmployeeId(savedEmployee.getId());
            audit.setAuditAction(AuditAction.EMPLOYEE_CREATED);
            audit.setCreatedAt(LocalDateTime.now());

            employeeAuditRepository.save(audit);
            return employeeMapper.toResponse(savedEmployee);
        });

    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeResponse findEmployeeById(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).
                orElseThrow(() -> new EmployeeNotFoundException("Employee with id " + employeeId + " not found"));

        if(log.isDebugEnabled()) {
            log.debug("Found Employee with id {} <employee>{}", employee.getId(), employee);
        }

        return employeeMapper.toResponse(employee);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeResponse> findAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        List<EmployeeResponse> responseList = new ArrayList<>();
        employees.forEach(employee -> responseList.add(employeeMapper.toResponse(employee)));
        return responseList;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmployeeResponse> getAllEmployees(String name, String department, BigDecimal minSalary, BigDecimal maxSalary, Status status, Pageable pageable) {
        Specification<Employee> specification = EmployeeSpecification.filter(name, department, minSalary, maxSalary, status);
        return employeeRepository.findAll(specification, pageable).map(employeeMapper::toResponse);
    }

    @Override
    public EmployeeResponse updateEmployee(EmployeeRequest request, Long employeeId) {
        Employee employeeToBeSaved = employeeRepository.findByIdForUpdate(employeeId).
                orElseThrow(() -> new EmployeeNotFoundException("Employee with id " + employeeId + " not found"));

        employeeMapper.updateEmployee(request,  employeeToBeSaved);

        return transactionTemplate.execute(status -> {
            Employee savedEmployee = employeeRepository.save(employeeToBeSaved);

            EmployeeAudit audit = new EmployeeAudit();
            audit.setEmployeeId(savedEmployee.getId());
            audit.setAuditAction(AuditAction.EMPLOYEE_UPDATED);
            audit.setUpdatedAt(LocalDateTime.now());

            employeeAuditRepository.save(audit);

            return employeeMapper.toResponse(savedEmployee);
        });
    }

    @Override
    public EmployeeResponse deleteEmployee(Long employeeId) {
        Employee employeeGoingToBeDeleted = employeeRepository.findByIdForUpdate(employeeId).
                orElseThrow(() -> new EmployeeNotFoundException("Employee with id " + employeeId + " not found"));

        return transactionTemplate.execute(status -> {
            employeeRepository.delete(employeeGoingToBeDeleted);

            EmployeeAudit audit = new EmployeeAudit();
            audit.setEmployeeId(employeeGoingToBeDeleted.getId());
            audit.setAuditAction(AuditAction.EMPLOYEE_DELETED);
            audit.setUpdatedAt(LocalDateTime.now());

            employeeAuditRepository.save(audit);

            return employeeMapper.toResponse(employeeGoingToBeDeleted);
        });
    }

}
