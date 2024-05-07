package com.example.kafka;

import org.apache.kafka.clients.producer.ProducerRecord;
import com.example.kafka.ScoreProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.kafka.core.KafkaTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ScoreProducerTest {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @Mock
    private Logger logger;

    @InjectMocks
    private ScoreProducer scoreProducer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testSendScore_Success() {
        // Test data
        String playerName = "player1";
        int score = 100;

        // Test
        scoreProducer.sendScore(playerName, score);

        // Verify
        verify(kafkaTemplate).send("score-topic", "player1,100");
    }

    @Test
    void testSendScore_Exception() {
        // Mock behavior to throw exception
        doThrow(new RuntimeException("Test exception")).when(kafkaTemplate).send(any(ProducerRecord.class));

        // Test
        try {
            scoreProducer.sendScore("player2", 200);
        } catch (RuntimeException e) {
            // Verify exception message and logging
            verify(logger).error("Error producing Kafka message", e);
            assertEquals("Error producing Kafka message", e.getMessage());
        }
    }
}
