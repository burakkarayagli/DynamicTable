package com.example.dynamictablebackend.kafka.configurations;

import com.example.dynamictablebackend.deserializers.KafkaMessageDeserializer;
import com.example.dynamictablebackend.deserializers.LogDeserializer;
import com.example.dynamictablebackend.logging.Log;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class LogConsumerConfiguration {


    @Bean
    ConcurrentKafkaListenerContainerFactory<String, Log> LogKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Log> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(LogConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, Log> LogConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(LogConsumerConfigurations(), new StringDeserializer(), new LogDeserializer());
    }

    @Bean
    public Map<String, Object> LogConsumerConfigurations() {
        Map<String, Object> configurations = new HashMap<>();
        configurations.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configurations.put(ConsumerConfig.GROUP_ID_CONFIG, "group-id");
        configurations.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configurations.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaMessageDeserializer.class);
        return configurations;
    }
}
