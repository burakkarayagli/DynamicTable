package com.example.dynamictablebackend.kafka;

import com.example.dynamictablebackend.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class LogConsumer {

    @KafkaListener(id = "kafkaLogListener", topics = "logging", groupId = "group1",
            containerFactory = "kafkaListenerContainerFactory", autoStartup = "true")
    public void consume(Log log) {
        System.out.println("Sending with KafkaListener: " + log);
    }

}
