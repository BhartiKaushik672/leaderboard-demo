package com.example.commons;

import com.example.db.entities.PlayerScore;
import com.example.db.repository.PlayerScoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommonUtilTest {

    @Mock
    private PlayerScoreRepository playerScoreRepository;

    @InjectMocks
    private CommonUtil commonUtil;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void checkIfPlayerAlreadyExists_PlayerExists_ReturnsTrue() {
        String playerName = "John";
        PlayerScore playerScore = new PlayerScore(0l,playerName, 100, "USA");

        when(playerScoreRepository.findPlayerScoreByPlayerName(playerName)).thenReturn(playerScore);

        assertTrue(commonUtil.checkIfPlayerAlreadyExists(playerName));
    }

    @Test
    void checkIfPlayerAlreadyExists_PlayerDoesNotExist_ReturnsFalse() {
        String playerName = "John";

        when(playerScoreRepository.findPlayerScoreByPlayerName(playerName)).thenReturn(null);

        assertFalse(commonUtil.checkIfPlayerAlreadyExists(playerName));
    }
}

