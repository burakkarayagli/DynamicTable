package com.example.dynamictablebackend;

import com.example.dynamictablebackend.kafka.LogProducer;
import com.example.dynamictablebackend.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ScheduledTask {

    @Autowired
    private LogProducer logProducer;

    //Possible levels
    List<String> levels = List.of("INFO", "DEBUG", "ERROR", "WARNING");
    //Possible loggers
    List<String> loggers = List.of("EYE", "GENIUS", "TUBE", "WIT", "KICK");
    //Possible messages
    List<String> messages = List.of("Creation successful",
            "Transfer successful",
            "Deletion successful",
            "Creation failed",
            "Transfer failed",
            "Transfer started",
            "Deletion started",
            "Creation started",
            "Random message");

    List<String> exceptions = List.of("Division by zero",
            "Null pointer exception",
            "Index out of bounds",
            "Random exception");

    List<String> users = List.of("John", "Jane", "Jack", "Jill", "Random user");

    List<String> data = List.of("Data1", "Data2", "Data3", "Data4", "Data5", "Data6", "Data7", "Data8", "Data9", "Data10", "Random data");

    public Log createRandomLog() {
        Log log = new Log();
        log.setTimestamp(String.valueOf(System.currentTimeMillis()));
        log.setLevel(levels.get((int) (Math.random() * levels.size())));
        log.setLogger(loggers.get((int) (Math.random() * loggers.size())));
        log.setMessage(messages.get((int) (Math.random() * messages.size())));

        if (log.getLevel().contains("ERROR")) {
            log.setException(exceptions.get((int) (Math.random() * exceptions.size())));
        }
        else {
            log.setException(null);
        }



        log.setUser(users.get((int) (Math.random() * users.size())));
        log.setData(data.get((int) (Math.random() * data.size())));
        return log;
    }

    @Scheduled(fixedRate = 1000)
    public void sendLog() {
        Log log = createRandomLog();
        System.out.println("Sending with Scheduled: " + log);
        //logProducer.publish(log);
    }

}
