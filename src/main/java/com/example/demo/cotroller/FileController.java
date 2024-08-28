package com.example.demo.cotroller;

import com.example.demo.service.FileService;
import com.example.demo.service.JpaEmService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class FileController {

    @Autowired
    FileService fileService;

    @Autowired
    JpaEmService jpaEMService;


    @PostMapping("/new-post")
    public ResponseEntity<Object> handleFile(HttpServletRequest request) throws IOException {
        return fileService.handleFile(request);

    }

    @GetMapping("/test-jpa")
    public String test() {

        jpaEMService.testJPA();

        return "SUCCESS";
    }

}