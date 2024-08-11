package com.example.demo.service;


import com.example.demo.entity.User;
import com.example.demo.repo.UserRepository;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.apache.catalina.connector.ClientAbortException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FileService {

    @Autowired
    UserRepository userRepository;


    public ResponseEntity<Object> handleFile(HttpServletRequest request) {

        try (ServletInputStream inputStream = request.getInputStream()) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            byte[] buffer = new byte[1024];
            int bytesRead;

            String line;
            List<User> userList = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                System.out.println(line);

                if (!line.startsWith("--") && !line.contains("Content-Disposition") && !line.contains("Content-Type:")
                        && !line.startsWith("Index,Customer phonenumber,First Name,Last Name,Company")
                        && !StringUtils.isEmpty(line)) {

                        String[] rows = line.split(",");
                        System.out.println(rows.toString());

                        if(rows != null && rows.length > 0) {
                            User user = User.builder()
                                    .name(Optional.ofNullable(rows[1]).orElse(""))
                                    .age(Optional.ofNullable(rows[2]).orElse(""))
                                    .email(Optional.ofNullable(rows[3]).orElse(""))
                                    .phoneNumber(Optional.ofNullable(rows[4]).orElse(""))
                                    .build();
                            userList.add(user);
                        }

                        if(userList.size() == 1000) {
                            saveUsersInBatch(userList);
                            userList.clear();
                        }
                }

            }
        } catch (ClientAbortException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.status(HttpStatus.OK).body(null);


    }


    @Transactional
    protected void saveUsersInBatch(List<User> userList) {
        userRepository.saveAllAndFlush(userList);
    }

}