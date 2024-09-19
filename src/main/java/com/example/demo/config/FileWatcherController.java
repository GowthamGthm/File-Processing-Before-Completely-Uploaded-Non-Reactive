package com.example.demo.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class FileWatcherController {


    @Autowired
    @Qualifier("myRestTemplate")
    RestTemplate restTemplate;

    @Autowired
    ConfigurableApplicationContext applicationContext;

    @EventListener
    public void handleUserCreatedEvent(CredsChangeEvent event) {
        System.out.println("File created event received in producer class: " + event.getFilePath());

        //        beanFactory.destroySingleton("myRestTemplate");

//        ConfigurableBeanFactory beanFactory = applicationContext.getBeanFactory();
//        beanFactory.destroyBean("myRestTemplate" , restTemplate);
//        beanFactory.registerSingleton("myRestTemplate", KafkaProducerHolder.createRestBean());
        DefaultSingletonBeanRegistry registry = (DefaultSingletonBeanRegistry) applicationContext.getAutowireCapableBeanFactory();
        registry.destroySingleton("myRestTemplate");
        registry.registerSingleton("myRestTemplate", KafkaProducerHolder.createRestBean());

    }

    @GetMapping("/test/r")
    public String checkRestTemplate() {
        System.out.println("Rest Template bean: " + restTemplate);
        System.out.println("Identity Hex String: " + ObjectUtils.getIdentityHexString(restTemplate));
        return ObjectUtils.getIdentityHexString(restTemplate);
    }


}
