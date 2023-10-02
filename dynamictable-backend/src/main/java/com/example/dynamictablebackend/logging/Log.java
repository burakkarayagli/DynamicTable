package com.example.dynamictablebackend.logging;

import java.io.Serializable;

public class Log implements Serializable {

    //Timestamp
    private String timestamp;
    //Level
    private String level;
    //Logger
    private String logger;
    //Message
    private String message;
    //Exception(If any)
    private String exception;
    //User(If any)
    private String user;
    //Data(If any)
    private String data;

    public Log() {
    }

    public Log(String timestamp, String level, String logger, String message, String exception, String user, String data) {
        this.timestamp = timestamp;
        this.level = level;
        this.logger = logger;
        this.message = message;
        this.exception = exception;
        this.user = user;
        this.data = data;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getLogger() {
        return logger;
    }

    public void setLogger(String logger) {
        this.logger = logger;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    //To String
    @Override
    public String toString() {
        return "Log{" +
                "timestamp='" + timestamp + '\'' +
                ", level='" + level + '\'' +
                ", logger='" + logger + '\'' +
                ", message='" + message + '\'' +
                ", exception='" + exception + '\'' +
                ", user='" + user + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
