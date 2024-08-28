package com.example.demo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "datasource")
@Data
public class DataSourcePropsConfig {


    private String url;
    private String password;
    private String username;
    private String driver;

    private String max_fetch_depth;
    private String fetch_size;
    private String batch_size;
    private String show_sql;
}
