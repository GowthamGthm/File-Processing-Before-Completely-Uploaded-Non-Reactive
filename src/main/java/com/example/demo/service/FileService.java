package com.example.demo.service;


import com.example.demo.entity.User;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.catalina.connector.ClientAbortException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class FileService {

    @Autowired
    AsyncDbSaver asyncDbSaver;

    private static final int LIST_BATCH_SIZE = 1000;


    public ResponseEntity<Object> handleFile(HttpServletRequest request) {

        try (ServletInputStream inputStream = request.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            String line;
            List<User> userList = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                System.out.println(line);

                if (isValidLine(line)) {
                    User user = createUserFromLine(line);
                    if (user != null) {
                        userList.add(user);
                    }

                    if (userList.size() == LIST_BATCH_SIZE) {
                        List<User> userCLoneList = new ArrayList<>(userList);
                        asyncDbSaver.saveUsersInBatch(userCLoneList);
                        userList.clear();
                    }
                }
            }
            System.out.println("finished reading file");
//            save remaining user list in DB
            if (!CollectionUtils.isEmpty(userList)) {
                asyncDbSaver.saveUsersInBatch(userList);
            }
        } catch (ClientAbortException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.status(HttpStatus.OK).body(null);


    }

    private User createUserFromLine(String line) {
        String[] rows = line.split(",");
//        System.out.println(Arrays.toString(rows));
        if (rows.length >= 5) {
            return User.builder()
                    .name(rows[1])
                    .age(rows[2])
                    .email(rows[3])
                    .phoneNumber(rows[4])
                    .build();
        }
        return null;
    }

    private boolean isValidLine(String line) {
        return (!line.startsWith("--")
                && !line.contains("Content-Disposition")
                && !line.contains("Content-Type:")
                && !line.startsWith("Index,Customer phonenumber,First Name,Last Name,Company")
                && StringUtils.hasText(line));
    }


}