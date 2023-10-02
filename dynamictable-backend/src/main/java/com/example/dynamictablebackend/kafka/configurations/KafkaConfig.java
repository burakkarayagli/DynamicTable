package com.example.dynamictablebackend.kafka.configurations;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConfig {

    private String serverAddress = "localhost:9092";

    @Bean
    public KafkaAdmin adminConfig() {
        Map<String, Object> config = new HashMap<>();
        config.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, serverAddress);
        return new KafkaAdmin(config);

    }

    @Bean
    public NewTopic topic() {
        return new NewTopic("test", 1, (short) 1);
    }





}
