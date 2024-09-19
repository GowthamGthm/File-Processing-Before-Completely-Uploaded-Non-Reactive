package com.example.demo.config;

import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.devtools.filewatch.FileSystemWatcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Path;
import java.time.Duration;

@Configuration
@Slf4j
public class CustomerFileWatcherConfig {


    @Autowired
    FileWatcherProperties properties;

    FileSystemWatcher fileSystemWatcher;

    @Bean
    FileSystemWatcher fileSystemWatcher(CustomerAddFileChangeListener fileChangeListener) {

         fileSystemWatcher = new FileSystemWatcher(
                properties.isDaemon(),
                Duration.ofMillis(properties.getPollInterval()),
                Duration.ofMillis(properties.getQuietPeriod()));

        fileSystemWatcher.addSourceDirectory(
                Path.of(properties.getDirectory()).toFile());

        fileSystemWatcher.addListener(fileChangeListener);

//        fileSystemWatcher.setTriggerFilter(
//                f -> f.toPath().startsWith("credentials"));
        fileSystemWatcher.start();

        log.info(String.format("FileSystemWatcher initialized. Monitoring directory %s",
                properties.getDirectory()));

        return fileSystemWatcher;
    }

    @PreDestroy
    public void onDestroy() throws Exception {
        log.info("Shutting Down File System Watcher.");
        if(fileSystemWatcher != null) {
            fileSystemWatcher.stop();
        }
    }

}