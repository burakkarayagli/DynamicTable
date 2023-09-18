package com.example.dynamictablebackend.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class Listener {

    @KafkaListener(topics = "test", groupId = "group1", containerFactory = "kafkaListenerContainerFactory")
    public void consume(String message) {
        System.out.println("Consumed message: " + message);
    }
}
