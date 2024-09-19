package com.example.demo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.devtools.filewatch.ChangedFile;
import org.springframework.boot.devtools.filewatch.ChangedFiles;
import org.springframework.boot.devtools.filewatch.FileChangeListener;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Set;

@Slf4j
@Component
public class CustomerAddFileChangeListener implements FileChangeListener {

    @Autowired
    ApplicationEventPublisher  publisher;

    @Override
    public void onChange(Set<ChangedFiles> changeSet) {

        for (ChangedFiles files : changeSet) {
            for (ChangedFile file : files.getFiles()) {
                if (file.getType().equals(ChangedFile.Type.MODIFY)) {
                    try {
                        if("credentials".equalsIgnoreCase(file.getRelativeName())) {
                            LocalTime received = LocalTime.now();
                            log.info("==================== File changed ====================");
                            Timestamp timestamp = new Timestamp(file.getFile().lastModified());
                            LocalTime localTime = timestamp.toLocalDateTime().toLocalTime();

                            System.out.println("last modified time of file : " + localTime.toString());
                            System.out.println("file received in code : " + received);
                            System.out.println("Difference: " + Duration.between(localTime , received).toMillis());

                            Files.readString(file.getFile().toPath());

                            publisher.publishEvent(new CredsChangeEvent(file.getFile().getAbsolutePath()));
                        }
                    } catch (IOException e) {
                        log.error("error reading file at path : {} , error: {}", file.getFile().toPath(), e);
                    }
                }
            }
        }
    }
}