package com.example.demo.service;

import com.example.demo.entity.Employee;
import com.example.demo.repo.EmployeeRepository;
import org.instancio.GeneratorSpecProvider;
import org.instancio.Instancio;
import org.instancio.generator.Generator;
import org.instancio.generator.GeneratorSpec;
import org.instancio.generator.specs.IntegerSpec;
import org.instancio.generator.specs.NumberGeneratorSpec;
import org.instancio.generators.Generators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.instancio.Select.field;

@Service
public class JpaEmService {

    @Autowired
    EmployeeRepository employeeRepository;


    public void testJPA() {



        List<Employee> empList = Instancio.ofList(Employee.class)
                .size(100)
                .set(field(Employee::getId) , null)
                .generate(field(Employee::getDeptId) , gen -> gen.ints().range(1,5))
                .generate(field(Employee::getAge) , gen -> gen.ints().range(25,80))
                .create();


        employeeRepository.saveAll(empList);

    }
}