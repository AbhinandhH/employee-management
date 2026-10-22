package com.assessment.employeemanagement.entity;

import com.assessment.employeemanagement.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "EMPLOYEE")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100, name = "EMP_NAME")
    private String name;

    @Column(nullable = false, length = 150, unique = true, name = "EMAIL")
    private String email;

    @Column(nullable = false, length = 100, name = "DEPT_NAME")
    private String department;

    @Column(nullable = false, precision = 12, scale = 2, name = "SALARY")
    private BigDecimal salary;

    @Column(nullable = false, length = 12, name = "PHONE_NUM", unique = true)
    private String phoneNumber;

    @Column(nullable = false, name = "AGE")
    private int age;

    @Column(nullable = false, name = "STATUS")
    @Enumerated(EnumType.STRING)
    private Status status;

}
