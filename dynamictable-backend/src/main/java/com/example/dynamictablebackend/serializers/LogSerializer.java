package com.example.dynamictablebackend.serializers;

import com.example.dynamictablebackend.logging.Log;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.kafka.common.serialization.Serializer;
public class LogSerializer implements Serializer<Log>{

    @Override
    public void configure(java.util.Map<java.lang.String,?> configs, boolean isKey) {
    }

    @Override
    public byte[] serialize(String test, Log log) {
        byte[] retVal = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
            objectMapper.registerModule(new JavaTimeModule());
            retVal = objectMapper.writeValueAsBytes(log);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;
    }

    @Override
    public void close() {
    }
}
