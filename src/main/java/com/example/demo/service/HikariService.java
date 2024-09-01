package com.example.demo.service;

import com.example.demo.entity.Managers;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
public class HikariService {

    @Autowired
    HikariDataSource hikariDataSource;

    @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
    private int batchSize;

    private int threadPoolSize = Runtime.getRuntime().availableProcessors() * 5;

    public static final String SQL = String.format(
            "INSERT INTO %s (name, age, dept_id, uuid) VALUES (?, ?, ?, ?)",
            Managers.class.getAnnotation(Table.class).name()
    );

    private static <T> List<List<T>> createSubList(List<T> list, int subListSize) {
        List<List<T>> listOfSubList = new ArrayList<>();
        for (int i = 0; i < list.size(); i += subListSize) {
            listOfSubList.add(list.subList(i, Math.min(i + subListSize, list.size())));
        }
        return listOfSubList;
    }

    public boolean saveAllJdbcBatchCallable(List<Managers> managerList, String uuid, Connection connection) throws ExecutionException, InterruptedException {
        System.out.println("Insert using JDBC batch, threading");
        ExecutorService executorService = Executors.newFixedThreadPool(threadPoolSize);

        List<List<Managers>> listOfBookSub = createSubList(managerList, 100);

        List<Callable<Boolean>> callables = listOfBookSub.stream().map(sublist ->
                (Callable<Boolean>) () -> saveAllJdbcBatch(sublist, uuid, connection)
        ).collect(Collectors.toList());

        boolean allSuccessful = true;

        try {
            List<Future<Boolean>> futures = executorService.invokeAll(callables);
            for (Future<Boolean> future : futures) {
                try {
                    Boolean result = future.get();
                    if (result) {
                        System.out.println("Batch saved successfully.");
                    } else {
                        System.out.println("Batch save failed.");
                        allSuccessful = false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    allSuccessful = false;
                    throw e;
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            allSuccessful = false;
            throw e;
        } finally {
            executorService.shutdown();
        }

        if (allSuccessful) {
            System.out.println("All batches were saved successfully.");
            return true;
        } else {
            System.out.println("Some batches failed to save.");
            return false;
        }
    }

    private boolean saveAllJdbcBatch(List<Managers> employeeList, String uuid, Connection connection) throws Exception {
        try (PreparedStatement statement = connection.prepareStatement(SQL)) {
            int counter = 0;
            for (Managers mangr : employeeList) {
                statement.setString(1, mangr.getName());
                statement.setInt(2, mangr.getAge());
                statement.setInt(3, mangr.getDeptId());
                statement.setString(4, uuid);
                statement.addBatch();

                if ((counter + 1) % batchSize == 0 || (counter + 1) == employeeList.size()) {
                    statement.executeBatch();
                    statement.clearBatch();
                }
                counter++;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public void delete(String uuid, Connection connection) throws Exception {
//        String sql = String.format(
//                "DELETE FROM %s WHERE uuid = ?",
//                Managers.class.getAnnotation(Table.class).name()
//        );
//
//        try (PreparedStatement statement = connection.prepareStatement(sql)) {
//            statement.setString(1, uuid);
//            int rowsAffected = statement.executeUpdate();
//            System.out.println("Rows deleted: " + rowsAffected);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}