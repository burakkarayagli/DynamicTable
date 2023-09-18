package com.example.dynamictablebackend.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class Publisher {


    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void publish(String message) {
        kafkaTemplate.send("test", message);
    }


}
