package com.example.dynamictablebackend.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class Listener {

    @Autowired
    SimpMessagingTemplate template;

    @KafkaListener(id = "kafkaMessageListener", topics = "test", groupId = "group1",
    containerFactory = "kafkaListenerContainerFactory", autoStartup = "true")
    public void consume(KafkaMessageDTO message) {
        System.out.println("Sending with KafkaListener: " + message);
        template.convertAndSend("/broadcast", message);
    }

}
