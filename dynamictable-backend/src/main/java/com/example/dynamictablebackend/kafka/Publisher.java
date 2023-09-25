package com.example.dynamictablebackend.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class Publisher {


    @Autowired
    private KafkaTemplate<String, KafkaMessageDTO> kafkaTemplate;

    public void publish(KafkaMessageDTO message) {
        kafkaTemplate.send("test", message);
    }


}
