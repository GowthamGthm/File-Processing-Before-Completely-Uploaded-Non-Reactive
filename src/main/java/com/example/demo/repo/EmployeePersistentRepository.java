package com.example.demo.repo;


import com.example.demo.entity.Employee;
import jakarta.persistence.EntityManager;
import jakarta.persistence.FlushModeType;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;


import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmployeePersistentRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private PlatformTransactionManager transactionManager;


    public TransactionStatus getStatusObject() {
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        return status;
    }


    public void saveEmployeeWithoutFlushing(List<Employee> employeeList , TransactionStatus status) throws InterruptedException {

        try {
            entityManager.setFlushMode(FlushModeType.COMMIT);
            for (Employee user : employeeList) {
                entityManager.persist(user);
            }
            System.out.println("------------------- done persisiting -----------------");
            System.out.println("------------------- will flush now -----------------" + LocalDateTime.now());
            System.out.println("------------------- flushed --------------------" + LocalDateTime.now());
        } catch (Exception e) {
            if (!status.isCompleted()) {
                transactionManager.rollback(status);
            }
            throw e;
        }
    }


    public void flushChanges(TransactionStatus status) {
        try {
            entityManager.flush();
            transactionManager.commit(status);
        } catch (Exception e) {
            if (!status.isCompleted()) {
                transactionManager.rollback(status);
            }
            throw e;
        }
    }

}