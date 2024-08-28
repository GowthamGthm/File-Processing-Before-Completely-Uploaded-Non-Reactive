package com.example.demo.utils;

import com.example.demo.entity.Employee;
import com.example.demo.entity.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.instancio.Instancio;
import org.instancio.Select;

import java.util.List;

import static org.instancio.Select.field;

public class Test {

    public static void main(String[] args) throws JsonProcessingException {

        List<Employee> empList = Instancio.ofList(Employee.class)
                .size(100)
                .set(field(Employee::getId) , null)
                .generate(field(Employee::getDeptId) , gen -> gen.ints().range(1,5))
                .create();

        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        System.out.println(mapper.writeValueAsString(empList));

    }
}