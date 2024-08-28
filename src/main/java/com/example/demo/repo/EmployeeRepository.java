package com.example.demo.repo;

import com.example.demo.entity.Employee;
import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}