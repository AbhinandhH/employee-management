package com.assessment.employeemanagement.service;

import com.assessment.employeemanagement.dto.request.EmployeeRequest;
import com.assessment.employeemanagement.dto.request.EmployeeUpdateRequest;
import com.assessment.employeemanagement.dto.response.EmployeeResponse;
import com.assessment.employeemanagement.entity.Employee;
import com.assessment.employeemanagement.entity.EmployeeAudit;
import com.assessment.employeemanagement.enums.Status;
import com.assessment.employeemanagement.exception.EmployeeNotFoundException;
import com.assessment.employeemanagement.mapper.EmployeeMapper;
import com.assessment.employeemanagement.repository.EmployeeAuditRepository;
import com.assessment.employeemanagement.repository.EmployeeRepository;
import com.assessment.employeemanagement.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeMapper employeeMapper;

    @Mock
    private TransactionTemplate transactionTemplate;

    @Mock
    private EmployeeAuditRepository employeeAuditRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void getEmployeeById_whenEmployeeDoesNotExist() {
        Long id = 100L;

        when(employeeRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                EmployeeNotFoundException.class,
                () -> employeeService.findEmployeeById(id)
        );

        verify(employeeRepository).findById(id);
    }

    @Test
    void createEmployee_shouldReturnEmployeeResponse() {
        when(transactionTemplate.execute(any(TransactionCallback.class)))
                .thenAnswer(invocation -> {
                    TransactionCallback<?> callback =
                            invocation.getArgument(0);
                    return callback.doInTransaction(null);
                });

        EmployeeRequest request = new EmployeeRequest(
                "Abhinandh",
                "abhinandh@example.com",
                "IT",
                "1234567890",
                300000.00,
                30,
                Status.ACTIVE
        );

        Employee savedEmployee = new Employee();
        savedEmployee.setId(1L);
        savedEmployee.setName("Abhinandh");
        savedEmployee.setEmail("abhinandh@example.com");
        savedEmployee.setDepartment("IT");
        savedEmployee.setPhoneNumber("1234567890");
        savedEmployee.setSalary(new BigDecimal("300000"));
        savedEmployee.setAge(30);
        savedEmployee.setStatus(Status.ACTIVE);

        EmployeeResponse expectedResponse = new EmployeeResponse(
                1L,
                "Abhinandh",
                "abhinandh@example.com",
                30,
                Status.ACTIVE,
                "IT",
                300000.00,
                "1234567890"
        );

        when(employeeRepository.save(any(Employee.class)))
                .thenReturn(savedEmployee);

        when(employeeMapper.toResponse(savedEmployee))
                .thenReturn(expectedResponse);

        EmployeeResponse actualResponse =
                employeeService.createEmployee(request);

        assertNotNull(actualResponse);
        assertEquals(1L, actualResponse.id());
        assertEquals(request.name(), actualResponse.name());
        assertEquals(request.email(), actualResponse.email());

        verify(employeeRepository).save(any(Employee.class));
        verify(employeeMapper).toResponse(savedEmployee);
    }

    @Test
    void updateEmployee_nameOnly_shouldKeepOtherFieldsUnchanged() {
        Long id = 1L;

        Employee employee = new Employee();
        employee.setId(id);
        employee.setName("Abhinandh");
        employee.setEmail("abhinandh@example.com");
        employee.setDepartment("IT");
        employee.setPhoneNumber("7884635272");
        employee.setSalary(new BigDecimal("30000"));
        employee.setAge(30);
        employee.setStatus(Status.ACTIVE);

        EmployeeUpdateRequest request = new EmployeeUpdateRequest(
                "Abhinandh H",
                null,
                null,
                null,
                null,
                null,
                null
        );

        EmployeeResponse expectedResponse = new EmployeeResponse(
                id,
                "Abhinandh H",
                "abhinandh@example.com",
                30,
                Status.ACTIVE,
                "IT",
                30000.00,
                "7884635272"
        );

        when(transactionTemplate.execute(any(TransactionCallback.class)))
                .thenAnswer(invocation -> {
                    TransactionCallback<?> callback = invocation.getArgument(0);
                    return callback.doInTransaction(null);
                });

        when(employeeRepository.findByIdForUpdate(id))
                .thenReturn(Optional.of(employee));

        when(employeeRepository.save(any(Employee.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        doAnswer(invocation -> {
            EmployeeUpdateRequest updateRequest = invocation.getArgument(0);
            Employee target = invocation.getArgument(1);

            if (updateRequest.name() != null) {
                target.setName(updateRequest.name());
            }
            if (updateRequest.email() != null) {
                target.setEmail(updateRequest.email());
            }
            if (updateRequest.department() != null) {
                target.setDepartment(updateRequest.department());
            }
            if (updateRequest.phoneNumber() != null) {
                target.setPhoneNumber(updateRequest.phoneNumber());
            }
            if (updateRequest.salary() != null) {
                target.setSalary(BigDecimal.valueOf(updateRequest.salary()));
            }
            if (updateRequest.age() != null) {
                target.setAge(updateRequest.age());
            }
            if (updateRequest.status() != null) {
                target.setStatus(updateRequest.status());
            }

            return null;
        }).when(employeeMapper).updateEmployee(
                any(EmployeeUpdateRequest.class),
                any(Employee.class)
        );

        when(employeeMapper.toResponse(any(Employee.class)))
                .thenReturn(expectedResponse);

        EmployeeResponse response = employeeService.updateEmployee(request, id);

        assertNotNull(response);
        assertEquals("Abhinandh H", employee.getName());
        assertEquals("abhinandh@example.com", employee.getEmail());
        assertEquals("IT", employee.getDepartment());
        assertEquals("7884635272", employee.getPhoneNumber());
        assertEquals(new BigDecimal("30000"), employee.getSalary());
        assertEquals(30, employee.getAge());
        assertEquals(Status.ACTIVE, employee.getStatus());

        assertEquals("Abhinandh H", response.name());

        verify(employeeRepository).findByIdForUpdate(id);
        verify(employeeMapper).updateEmployee(request, employee);
        verify(employeeRepository).save(employee);
        verify(employeeAuditRepository).save(any(EmployeeAudit.class));
        verify(employeeMapper).toResponse(employee);
    }

    @Test
    void updateEmployee_missingId_shouldThrowException() {
        Long id = 100L;

        EmployeeUpdateRequest request = new EmployeeUpdateRequest(
                "Rahul",
                null,
                null,
                null,
                null,
                null,
                null
        );

        when(transactionTemplate.execute(any(TransactionCallback.class)))
                .thenAnswer(invocation -> {
                    TransactionCallback<?> callback = invocation.getArgument(0);
                    return callback.doInTransaction(null);
                });

        when(employeeRepository.findByIdForUpdate(id))
                .thenReturn(Optional.empty());

        assertThrows(
                EmployeeNotFoundException.class,
                () -> employeeService.updateEmployee(request, id)
        );

        verify(employeeRepository).findByIdForUpdate(id);
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    void deleteEmployee_existingId_shouldDeleteSuccessfully() {
        when(transactionTemplate.execute(any(TransactionCallback.class)))
                .thenAnswer(invocation -> {
                    TransactionCallback<?> callback =
                            invocation.getArgument(0);
                    return callback.doInTransaction(null);
                });

        Long id = 1L;

        Employee employee = new Employee();
        employee.setId(id);
        employee.setName("Abhinandh");
        employee.setEmail("abhinandh@example.com");
        employee.setDepartment("IT");
        employee.setSalary(new BigDecimal("30000"));

        when(employeeRepository.findByIdForUpdate(id))
                .thenReturn(Optional.of(employee));

        employeeService.deleteEmployee(id);

        verify(employeeRepository).findByIdForUpdate(id);
        verify(employeeRepository).delete(employee);
    }

    @Test
    void deleteEmployee_missingId_shouldThrowException() {
        when(transactionTemplate.execute(any(TransactionCallback.class)))
                .thenAnswer(invocation -> {
                    TransactionCallback<?> callback =
                            invocation.getArgument(0);
                    return callback.doInTransaction(null);
                });

        Long id = 100L;

        when(employeeRepository.findByIdForUpdate(id))
                .thenReturn(Optional.empty());

        assertThrows(
                EmployeeNotFoundException.class,
                () -> employeeService.deleteEmployee(id)
        );

        verify(employeeRepository).findByIdForUpdate(id);
        verify(employeeRepository, never()).delete(any(Employee.class));
    }
}