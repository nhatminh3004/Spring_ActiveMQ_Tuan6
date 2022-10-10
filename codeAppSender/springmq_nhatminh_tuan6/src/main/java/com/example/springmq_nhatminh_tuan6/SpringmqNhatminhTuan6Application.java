package com.example.springmq_nhatminh_tuan6;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jms.activemq.ActiveMQAutoConfiguration;

@SpringBootApplication(exclude = ActiveMQAutoConfiguration.class)

public class SpringmqNhatminhTuan6Application {

    public static void main(String[] args) {
        SpringApplication.run(SpringmqNhatminhTuan6Application.class, args);
    }

}
