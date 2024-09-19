package com.example.demo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@ConfigurationProperties(prefix = "application.file.watch")
@Component
@Data
public class FileWatcherProperties {

    String directory;
    boolean daemon;
    Long pollInterval;
    Long quietPeriod;
}