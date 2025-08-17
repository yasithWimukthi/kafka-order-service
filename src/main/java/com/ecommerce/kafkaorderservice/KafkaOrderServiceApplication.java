package com.ecommerce.kafkaorderservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class KafkaOrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(KafkaOrderServiceApplication.class, args);
    }

}
