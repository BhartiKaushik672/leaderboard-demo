package com.example.kafka;

import com.example.commons.CommonUtil;
import com.example.db.entities.PlayerScore;
import com.example.db.repository.PlayerScoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class ScoreConsumerTest {

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private CommonUtil commonUtil;

    @Mock
    private PlayerScoreRepository playerScoreRepository;

    @Mock
    private Logger logger;

    @InjectMocks
    private ScoreConsumer scoreConsumer;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testConsumeSQL_PlayerExists() {
        // Mocking the repository behavior
        PlayerScore playerScore = new PlayerScore();
        when(playerScoreRepository.findPlayerScoreByPlayerName("player1")).thenReturn(playerScore);

        // Calling the method under test
        scoreConsumer.consumeSQL("player1,100");

        // Verifying that the repository method was called
        verify(playerScoreRepository, times(1)).findPlayerScoreByPlayerName("player1");
    }


    @Test
    public void testConsumeSQL_PlayerDoesNotExist() {
        // Arrange
        String message = "nonexistent_player,100";
        when(playerScoreRepository.findPlayerScoreByPlayerName("nonexistent_player")).thenReturn(null);

        // Act
        scoreConsumer.consumeSQL(message);

        // Assert
        verify(playerScoreRepository, never()).save(any(PlayerScore.class));
    }

    @Test
    public void testConsumeSQL_InvalidMessageFormat() {
        // Arrange
        String message = "invalid_message_format";

        // Act
        scoreConsumer.consumeSQL(message);

        // Assert
        // Ensure that the method does not attempt to update player scores in SQL for an invalid message format
        verify(playerScoreRepository, never()).findPlayerScoreByPlayerName(anyString());
    }

}
