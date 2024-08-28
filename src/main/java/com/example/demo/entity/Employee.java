package com.example.demo.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "tbl_employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq-employee-gen")
    @SequenceGenerator(name = "seq-employee-gen", sequenceName = "seq-employee",
            initialValue = 1, allocationSize = 1)
    private Long id;

    private String name;

    private Integer age;

    @Column(name = "dept_id")
    private Integer deptId;

}