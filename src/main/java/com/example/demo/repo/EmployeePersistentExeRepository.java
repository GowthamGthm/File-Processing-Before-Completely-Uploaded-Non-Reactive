package com.example.demo.repo;


import com.example.demo.entity.Employee;
import jakarta.persistence.EntityManager;
import jakarta.persistence.FlushModeType;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class EmployeePersistentExeRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public void saveEmployeeWithoutFlushing(List<Employee> employeeList) throws InterruptedException {
        entityManager.setFlushMode(FlushModeType.COMMIT);
        for (Employee user : employeeList) {
            entityManager.persist(user);
        }
        System.out.println("------------------- done persisiting -----------------");
//        System.out.println("------------------- will flush now -----------------" + LocalDateTime.now());
//        System.out.println("------------------- flushed --------------------" + LocalDateTime.now());
    }

    public void flushChanges() {
        entityManager.flush();
    }

}