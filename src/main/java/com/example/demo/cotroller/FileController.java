package com.example.demo.cotroller;

import com.example.demo.entity.Employee;
import com.example.demo.entity.Managers;
import com.example.demo.service.FileService;
import com.example.demo.service.HikariService;
import com.example.demo.service.JpaEmService;
import com.example.demo.service.JpaExecutorService;
import jakarta.servlet.http.HttpServletRequest;
import org.instancio.Instancio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static org.instancio.Select.field;

@RestController
public class FileController {

    @Autowired
    FileService fileService;

    @Autowired
    JpaEmService jpaEMService;

    @Autowired
    JpaExecutorService jpaExecutorService;

    @Autowired
    HikariService hikariService;


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

    @GetMapping("/test-jdbc")
    public String jdbc() throws Exception {
        for (int i = 0; i < 50; i++) {
            List<Managers> empList = Instancio.ofList(Managers.class)
                    .size(1000)
                    .set(field(Managers::getId), null)
                    .generate(field(Managers::getDeptId), gen -> gen.ints().range(1, 5))
                    .generate(field(Managers::getAge), gen -> gen.ints().range(25, 80))
                    .create();

//            simulating error in the mid of saving , trying to save invalid foreign key
//            if(i == (MAX- 5)) {
//                empList.get(50).setDeptId(7);
//            }

            hikariService.saveAllJdbcBatch(empList , UUID.randomUUID().toString());

        }
        return "SUCCESS";
    }

    @GetMapping("/test-jdbc-one")
    public String jdbcOne() throws Exception {
        int MAX = 100;
        List<Boolean> boolList = new ArrayList<>();
        String uuid = UUID.randomUUID().toString();

        for (int i = 0; i < MAX; i++) {
            List<Managers> empList = Instancio.ofList(Managers.class)
                    .size(1000)
                    .set(field(Managers::getId), null)
                    .set(field(Managers::getUuid), uuid)
                    .generate(field(Managers::getDeptId), gen -> gen.ints().range(1, 5))
                    .generate(field(Managers::getAge), gen -> gen.ints().range(25, 80))
                    .create();

//            simulating error in the mid of saving , trying to save invalid foreign key
            if(i == (MAX- 5)) {
                empList.get(50).setDeptId(7);
            }

            boolean result = hikariService.saveAllJdbcBatchCallable(empList, uuid);
            boolList.add(result);
        }


        boolean failed = boolList.stream().filter(ele -> !ele).findFirst().orElse(false);
        if(failed) {
            System.out.println("------------------ found some failed DB calls , so clearing everything ");
            hikariService.delete(uuid);
        }
        return "SUCCESS";
    }

}
