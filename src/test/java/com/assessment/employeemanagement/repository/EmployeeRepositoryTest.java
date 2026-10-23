package com.assessment.employeemanagement.repository;

import com.assessment.employeemanagement.entity.Employee;
import com.assessment.employeemanagement.enums.Status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
public class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    void saveEmployee_shouldReturnSavedEmployee() {
        Employee employee = new Employee();
        employee.setName("Abhinandh");
        employee.setEmail("abhinandh@example.com");
        employee.setDepartment("IT");
        employee.setAge(30);
        employee.setPhoneNumber("8989898989");
        employee.setStatus(Status.ACTIVE);
        employee.setSalary(new BigDecimal(30000));

        Employee savedEmployee = employeeRepository.save(employee);

        assertNotNull(savedEmployee.getId());
        assertEquals("Abhinandh", savedEmployee.getName());
        assertEquals("abhinandh@example.com", savedEmployee.getEmail());
    }
}
