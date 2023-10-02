package com.example.dynamictablebackend.kafka;

import com.example.dynamictablebackend.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class LogProducer {

    @Autowired
    private KafkaTemplate<String, Log> LogKafkaTemplate;

    public void publish(Log log) {
        LogKafkaTemplate.send("logging", log);
    }
}
