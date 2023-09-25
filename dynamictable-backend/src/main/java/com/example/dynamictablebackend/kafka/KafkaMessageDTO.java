package com.example.dynamictablebackend.kafka;

import java.io.Serializable;

public class KafkaMessageDTO implements Serializable{
    private String message;
    private String status;


    public KafkaMessageDTO(String message, String status) {
        this.message = message;
        this.status = status;
    }

    public KafkaMessageDTO() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
