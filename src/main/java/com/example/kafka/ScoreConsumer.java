package com.example.kafka;

import com.example.commons.CommonUtil;
import com.example.db.entities.PlayerScore;
import com.example.db.repository.PlayerScoreRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ScoreConsumer {
    private final RedisTemplate<String, String> redisTemplate;
    private final CommonUtil commonUtil;
    private final PlayerScoreRepository playerScoreRepository;
    private final Logger logger = LoggerFactory.getLogger(ScoreConsumer.class);

    @Autowired
    public ScoreConsumer(RedisTemplate<String, String> redisTemplate, CommonUtil commonUtil, PlayerScoreRepository playerScoreRepository) {
        this.redisTemplate = redisTemplate;
        this.commonUtil = commonUtil;
        this.playerScoreRepository = playerScoreRepository;
    }

    @KafkaListener(topics = "score-topic", groupId = "score_group_id_redis")
    public void consumeRedis(String message) {
        try {
            String[] parts = message.split(",");
            if (parts.length != 2) {
                throw new IllegalArgumentException("Invalid message format");
            }
            String playerName = parts[0];
            int score = Integer.parseInt(parts[1]);
            updateTopScores(playerName, score);
            logger.info("Message consumed in Redis: {}", message);
        } catch (Exception e) {
            logger.error("Error processing Kafka message for Redis", e);
        }
    }

    @KafkaListener(topics = "score-topic", groupId = "score_group_id_sql")
    public void consumeSQL(String message) {
        try {
            String[] parts = message.split(",");
            if (parts.length != 2) {
                throw new IllegalArgumentException("Invalid message format");
            }
            String playerName = parts[0];
            int score = Integer.parseInt(parts[1]);
            PlayerScore playerScore = playerScoreRepository.findPlayerScoreByPlayerName(playerName);
            if (playerScore != null) {
                playerScore.setScore(score);
                playerScore.setLastUpdatedOn(LocalDateTime.now());
                playerScoreRepository.save(playerScore);
                logger.info("Message consumed in SQL: {}", message);
            } else {
                logger.error("User does not exist: {}", playerName);
            }
        } catch (Exception e) {
            logger.error("Error processing Kafka message for SQL", e);
        }
    }

    private void updateTopScores(String playerName, int score) {
        try {
            String sortedSetKey = "players-top-scores";
            redisTemplate.opsForZSet().add(sortedSetKey, playerName, score);
        } catch (Exception e) {
            logger.error("Error updating top scores in Redis", e);
        }
    }
}
