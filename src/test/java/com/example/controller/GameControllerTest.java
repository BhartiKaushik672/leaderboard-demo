package com.example.controller;

import com.example.dto.LeaderboardEntryDTO;
import com.example.model.PlayerDetails;
import com.example.service.PlayerService;
import com.example.service.TopScoresService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class GameControllerTest {

    @Mock
    private PlayerService playerService;

    @Mock
    private TopScoresService topScoresService;

    @InjectMocks
    private GameController gameController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addUser_Success() {
        ResponseEntity<Void> response = gameController.addUser("John", "USA");
        assertEquals(200, response.getStatusCodeValue());
        verify(playerService, times(1)).addUser(anyString(), anyString());
    }

    @Test
    void submitScore_Success() {
        ResponseEntity<Void> response = gameController.submitScore("John", 100);
        assertEquals(200, response.getStatusCodeValue());
        verify(playerService, times(1)).submitScore(anyString(), anyInt());
    }

    @Test
    void getPlayerScore_Success() {
        PlayerDetails playerDetails = PlayerDetails.builder().playerName("john").country("test").build();
        when(playerService.getPlayerScore(anyString())).thenReturn(playerDetails);

        ResponseEntity<PlayerDetails> response = gameController.getPlayerScore("John");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(playerDetails, response.getBody());
        verify(playerService, times(1)).getPlayerScore(anyString());
    }

    @Test
    void getTopScores_Success() {
        LeaderboardEntryDTO leaderboardEntryDTO = new LeaderboardEntryDTO("John", 100);
        List<LeaderboardEntryDTO> topScores = Collections.singletonList(leaderboardEntryDTO);
        when(topScoresService.getTopScores()).thenReturn(topScores);

        ResponseEntity<List<LeaderboardEntryDTO>> response = gameController.getTopScores();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(topScores, response.getBody());
        verify(topScoresService, times(1)).getTopScores();
    }
}
