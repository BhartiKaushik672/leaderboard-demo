package com.example.service;

import com.example.commons.CommonUtil;
import com.example.db.entities.PlayerScore;
import com.example.db.repository.PlayerScoreRepository;
import com.example.kafka.ScoreProducer;
import com.example.mappers.PlayerScoresMapper;
import com.example.model.PlayerDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PlayerServiceTest {

    @Mock
    private PlayerScoreRepository playerScoreRepository;

    @Mock
    private ScoreProducer scoreProducer;

    @Mock
    private PlayerScoresMapper playerScoresMapper;

    @Mock
    private CommonUtil common;

    @InjectMocks
    private PlayerService playerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void addUser_Success() {
        String playerName = "John";
        String country = "USA";
        PlayerDetails playerDetails = PlayerDetails.builder().playerName(playerName).country(country).build();
        PlayerScore playerScore = new PlayerScore();

        when(common.checkIfPlayerAlreadyExists(playerName)).thenReturn(false);
        when(playerScoresMapper.maptoPlayerSCore(playerDetails)).thenReturn(playerScore);

        assertDoesNotThrow(() -> playerService.addUser(playerName, country));

        verify(scoreProducer, times(1)).sendScore(playerName, 0);
    }

    @Test
    void addUser_PlayerAlreadyExists_ThrowsException() {
        String playerName = "John";
        String country = "USA";

        when(common.checkIfPlayerAlreadyExists(playerName)).thenReturn(true);

        assertThrows(RuntimeException.class, () -> playerService.addUser(playerName, country));

        verify(playerScoreRepository, never()).save(any());
        verify(scoreProducer, never()).sendScore(anyString(), anyInt());
    }

    @Test
    void submitScore_Success() {
        String playerName = "John";
        int score = 100;

        assertDoesNotThrow(() -> playerService.submitScore(playerName, score));

        verify(scoreProducer, times(1)).sendScore(playerName, score);
    }

    @Test
    void getPlayerScore_PlayerFound_ReturnsPlayerDetails() {
        String playerName = "John";
        String country = "USA";
        PlayerScore playerScore = new PlayerScore(0L,playerName,100, country );

        when(playerScoreRepository.findPlayerScoreByPlayerName(playerName)).thenReturn(playerScore);

        PlayerDetails result = playerService.getPlayerScore(playerName);

        assertNotNull(result);
        assertEquals(playerName, result.getPlayerName());
        assertEquals(country, result.getCountry());
        assertEquals(playerScore.getScore(), result.getScore());
    }

    @Test
    void getPlayerScore_PlayerNotFound_ThrowsException() {
        String playerName = "John";

        when(playerScoreRepository.findPlayerScoreByPlayerName(playerName)).thenReturn(null);

        assertThrows(RuntimeException.class, () -> playerService.getPlayerScore(playerName));
    }
}
