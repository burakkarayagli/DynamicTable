package com.example.dynamictablebackend.kafka;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.kafka.common.serialization.Deserializer;
public class KafkaMessageDeserializer implements Deserializer<KafkaMessageDTO>{



    @Override
    public void configure(java.util.Map<java.lang.String,?> configs, boolean isKey) {
    }


    @Override
    public KafkaMessageDTO deserialize(String test, byte[] bytes) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            mapper.registerModule(new JavaTimeModule());
            return mapper.readValue(bytes, KafkaMessageDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void close() {
    }


}
