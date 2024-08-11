package com.example.demo.service;


import com.example.demo.entity.User;
import com.example.demo.repo.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AsyncDbSaver {

    @Autowired
    UserRepository userRepository;

    @Async
    @Transactional
    public void saveUsersInBatch(List<User> userList) {
        userRepository.saveAllAndFlush(userList);
    }

}