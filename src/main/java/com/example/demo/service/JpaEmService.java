package com.example.demo.service;

import com.example.demo.entity.Employee;
import com.example.demo.repo.EmployeePersistentRepository;
import com.example.demo.repo.EmployeeRepository;
import org.instancio.Instancio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;

import java.util.List;

import static org.instancio.Select.field;

@Service
public class JpaEmService {

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    EmployeePersistentRepository employeePersistentRepository;


    public void saveOne() {



        List<Employee> empList = Instancio.ofList(Employee.class)
                .size(100)
                .set(field(Employee::getId) , null)
                .generate(field(Employee::getDeptId) , gen -> gen.ints().range(1,5))
                .generate(field(Employee::getAge) , gen -> gen.ints().range(25,80))
                .create();


        employeeRepository.saveAll(empList);

    }


    public void saveTwo() throws InterruptedException {

        List<Employee> empList = Instancio.ofList(Employee.class)
                .size(100)
                .set(field(Employee::getId) , null)
                .generate(field(Employee::getDeptId) , gen -> gen.ints().range(1,5))
                .generate(field(Employee::getAge) , gen -> gen.ints().range(25,80))
                .create();

        TransactionStatus status = employeePersistentRepository.getStatusObject();
        employeePersistentRepository.saveEmployeeWithoutFlushing(empList , status);

        System.out.println("==================== DAO TASK DONE ==============");
        Thread.sleep(30  * 1000);
        employeePersistentRepository.flushChanges(status);

    }

    public void saveThree() throws InterruptedException {

        TransactionStatus status = employeePersistentRepository.getStatusObject();
        for(int i = 0 ; i < 100 ; i++) {
            List<Employee> empList = Instancio.ofList(Employee.class)
                    .size(100)
                    .set(field(Employee::getId) , null)
                    .generate(field(Employee::getDeptId) , gen -> gen.ints().range(1,5))
                    .generate(field(Employee::getAge) , gen -> gen.ints().range(25,80))
                    .create();
            employeePersistentRepository.saveEmployeeWithoutFlushing(empList , status);

        }
        System.out.println("==================== DAO TASK DONE ==============");
        Thread.sleep(30  * 1000);
        employeePersistentRepository.flushChanges(status);

    }

    public void saveFour() throws InterruptedException {
        int MAX = 50;

        TransactionStatus status = employeePersistentRepository.getStatusObject();
        for(int i = 0 ; i < MAX  ; i++) {
            List<Employee> empList = Instancio.ofList(Employee.class)
                    .size(100)
                    .set(field(Employee::getId) , null)
                    .generate(field(Employee::getDeptId) , gen -> gen.ints().range(1,5))
                    .generate(field(Employee::getAge) , gen -> gen.ints().range(25,80))
                    .create();

//            simulating error in the mid of saving , trying to save invalid foreign key
            if(i == (MAX- 5)) {
                empList.get(50).setDeptId(7);
            }
            employeePersistentRepository.saveEmployeeWithoutFlushing(empList , status);

        }
        System.out.println("==================== DAO TASK DONE ==============");
        Thread.sleep(30  * 1000);
        employeePersistentRepository.flushChanges(status);

    }

    public void flush() {
        TransactionStatus status = employeePersistentRepository.getStatusObject();
        employeePersistentRepository.flushChanges(status);
    }
}