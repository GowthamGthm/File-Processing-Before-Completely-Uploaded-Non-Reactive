package com.example.demo.service;

import com.example.demo.entity.Employee;
import com.example.demo.repo.EmployeePersistentExeRepository;
import com.example.demo.repo.EmployeePersistentRepository;
import com.example.demo.repo.EmployeeRepository;
import org.instancio.Instancio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.instancio.Select.field;

@Service
public class JpaExecutorService {

    @Autowired
    EmployeePersistentRepository employeePersistentRepository;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    EmployeePersistentExeRepository employeePersistentExeRepository;

    @Autowired
    PlatformTransactionManager transactionManager;


    public void saveUsingExecutor(boolean generateError) throws InterruptedException, ExecutionException {

        int LIST_SIZE = 5005;

        TransactionStatus status = employeePersistentRepository.getStatusObject();

        List<Employee> empList = Instancio.ofList(Employee.class)
                    .size(LIST_SIZE)
                    .set(field(Employee::getId) , null)
                    .generate(field(Employee::getDeptId) , gen -> gen.ints().range(1,5))
                    .generate(field(Employee::getAge) , gen -> gen.ints().range(25,60))
                    .create();

        // generating error on condition, invalid foreign key dept_id
        if(generateError) {
            empList.get(LIST_SIZE - 5).setDeptId(7);
        }


        int sublistSize = (int) Math.ceil((double) empList.size() / 10);
        List<List<Employee>> sublists = new ArrayList<>();
        for (int i = 0; i < empList.size(); i += sublistSize) {
            sublists.add(empList.subList(i, Math.min(i + sublistSize, empList.size())));
        }

        // Create an ExecutorService with a fixed thread pool
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        // Create a list to hold Future objects
        List<Future<Void>> futures = new ArrayList<>();

        // Submit tasks to the executor service
        for (List<Employee> sublist : sublists) {
            Callable<Void> task = () -> {
                employeePersistentRepository.saveEmployeeWithoutFlushing(sublist , status);
                return null;
            };
            futures.add(executorService.submit(task));
        }

        // Wait for all tasks to complete
        for (Future<Void> future : futures) {
            try {
                future.get();
            } catch (InterruptedException  | ExecutionException e) {
                throw e;
            }
        }

        // Shutdown the executor service
        executorService.shutdown();

        System.out.println("==================== DAO TASK DONE ==============");
        Thread.sleep(30  * 1000);
        employeePersistentRepository.flushChanges(status);

    }






}