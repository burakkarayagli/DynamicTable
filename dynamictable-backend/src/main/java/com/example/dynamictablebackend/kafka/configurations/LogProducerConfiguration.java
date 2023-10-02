package com.example.dynamictablebackend.kafka.configurations;

import com.example.dynamictablebackend.logging.Log;
import com.example.dynamictablebackend.serializers.KafkaMessageSerializer;
import com.example.dynamictablebackend.serializers.LogSerializer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class LogProducerConfiguration {
    @Bean
    public ProducerFactory<String, Log> LogProducerFactory() {
        return new DefaultKafkaProducerFactory<>(LogProducerConfigurations());
    }

    @Bean
    public Map<String, Object> LogProducerConfigurations() {
        Map<String, Object> configurations = new HashMap<>();
        configurations.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configurations.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configurations.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, LogSerializer.class);
        return configurations;
    }

    @Bean
    public KafkaTemplate<String, Log> LogKafkaTemplate() {
        return new KafkaTemplate<>(LogProducerFactory());
    }
}
