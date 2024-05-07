package com.example.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class ScoreProducer {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private static final Logger LOGGER = LoggerFactory.getLogger(ScoreProducer.class);

    public void sendScore(String playerName, int score) {
        try {
            String message = playerName + "," + score;
            LOGGER.info(String.format("Message produced is %s", message));
            kafkaTemplate.send("score-topic", message);
        } catch (Exception e) {

            // For now, logging the exception and continue
            LOGGER.error("Error producing Kafka message", e);

            throw new RuntimeException("Error producing Kafka message", e);
        }
    }
}
