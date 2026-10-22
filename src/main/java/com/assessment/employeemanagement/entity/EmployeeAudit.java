package com.assessment.employeemanagement.entity;

import com.assessment.employeemanagement.enums.AuditAction;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "EMPLOYEE_AUDIT")
@Getter
@Setter
@NoArgsConstructor
public class EmployeeAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "EMP_ID")
    private Long employeeId;

    @Column(name = "ACTION", nullable = false)
    @Enumerated(EnumType.STRING)
    private AuditAction auditAction;

    @Column(name = "CREATEDT")
    private LocalDateTime createdAt;

    @Column(name = "UPDATEDT")
    private LocalDateTime updatedAt;

}
