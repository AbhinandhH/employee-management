package com.assessment.employeemanagement.repository;

import com.assessment.employeemanagement.entity.EmployeeAudit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeAuditRepository extends JpaRepository<EmployeeAudit, Long> {
}
