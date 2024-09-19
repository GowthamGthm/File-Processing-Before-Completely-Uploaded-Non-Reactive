package com.example.demo.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;


/**
 * Holder class for managing the current KafkaProducer instance.
 */
@Configuration
public class KafkaProducerHolder {


//    private RestTemplate restTemplate;


//    @PostConstruct
//    public void buildKafkaProducer() {
//        resetKafkaProducer();
//        setUpKafkaProducer();
//    }

//    @Bean("myRestTemplate")
//    private RestTemplate setUpKafkaProducer() {
//        System.out.println("Setting up KafkaProducer");
//        restTemplate = new RestTemplate();
//        System.out.println("restTemplate from setUpKafkaProducer: "  +restTemplate);
//        return restTemplate;
//    }

    @Bean("myRestTemplate")
    public RestTemplate setUpKafkaProducer() {
       return createRestBean();
    }

    public static RestTemplate createRestBean() {
        System.out.println("Setting up KafkaProducer");
        RestTemplate restTemplate = new RestTemplate();
        System.out.println("restTemplate from setUpKafkaProducer: "  +restTemplate);
        return restTemplate;
    }

//    private void resetKafkaProducer() {
//        System.out.println("restTemplate from resetKafkaProducer: "  +restTemplate);
//        if( restTemplate != null ) {
//            System.out.println("resetting and closing rest template");
//        }
//    }


}