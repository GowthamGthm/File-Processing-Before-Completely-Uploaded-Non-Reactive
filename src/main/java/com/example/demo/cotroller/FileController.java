package com.example.demo.cotroller;

import com.example.demo.service.FileService;
import com.example.demo.service.JpaEmService;
import com.example.demo.service.JpaExecutorService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@RestController
public class FileController {

    @Autowired
    FileService fileService;

    @Autowired
    JpaEmService jpaEMService;

    @Autowired
    JpaExecutorService jpaExecutorService;


    @PostMapping("/new-post")
    public ResponseEntity<Object> handleFile(HttpServletRequest request) throws IOException {
        return fileService.handleFile(request);

    }

    @GetMapping("/test-save")
    public String save() throws InterruptedException {

        jpaEMService.saveFour();

        return "SUCCESS";
    }

    @GetMapping("/test-exe/{fail}")
    public String executor(@PathVariable boolean fail) throws InterruptedException, ExecutionException {

        jpaExecutorService.saveUsingExecutor(fail);

        return "SUCCESS";
    }

    @GetMapping("/test-flush")
    public String flush() throws InterruptedException {

        jpaEMService.flush();

        return "SUCCESS";
    }

}