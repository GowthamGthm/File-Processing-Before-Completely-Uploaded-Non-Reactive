package com.example.demo.service;


import com.example.demo.entity.User;
import com.example.demo.repo.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AsyncDbSaver {

    @Autowired
    UserRepository userRepository;

    @Value("${batch.size:50}")
    Integer batchSize;


    @Async
    @Transactional
    public void saveUsersInBatch(List<User> userList) {
        System.out.println("Async batch size: " + batchSize);
        for (int i = 0; i < userList.size(); i = i + batchSize) {
            if( i+ batchSize > userList.size()){
                List<User> user1 = userList.subList(i, userList.size() - 1);
                userRepository.saveAllAndFlush(user1);
                break;
            }

            List<User> user1 = userList.subList(i, i + batchSize);
            userRepository.saveAllAndFlush(user1);
        }
        userRepository.saveAllAndFlush(userList);
    }

}