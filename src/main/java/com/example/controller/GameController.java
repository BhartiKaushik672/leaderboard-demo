package com.example.controller;

import com.example.dto.LeaderboardEntryDTO;
import com.example.model.PlayerDetails;
import com.example.service.PlayerService;
import com.example.service.TopScoresService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/leaderboard")
public class GameController {
    @Autowired
    private PlayerService playerService;

    @Autowired
    private TopScoresService topScoresService;

    @Operation(summary = "API to add user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Accepted"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "500", description = "Unable to complete request. Please contact support team.")
    })
    @PostMapping("/addUser")
    public ResponseEntity<Void> addUser(@RequestHeader String playerName, @RequestHeader String country) {
        playerService.addUser(playerName,country);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "API to update player's score.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Accepted"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "500", description = "Unable to complete request. Please contact support team.")
    })
    @PutMapping("/score")
    public ResponseEntity<Void> submitScore(@RequestHeader String playerName, @RequestHeader int score) {
        playerService.submitScore(playerName,score);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "API to get player's score and details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Accepted"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "500", description = "Unable to complete request. Please contact support team.")
    })
    @GetMapping("/score/{playerName}")
    public ResponseEntity<PlayerDetails> getPlayerScore(@PathVariable("playerName") String playerName) {
        PlayerDetails topScores = playerService.getPlayerScore(playerName);
        return ResponseEntity.status(topScores == null? HttpStatus.NOT_FOUND : HttpStatus.OK).body(topScores);
    }

    @Operation(summary = "API to get the top 5 players on leaderboard")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Accepted"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "500", description = "Unable to complete request. Please contact support team.")
    })
    @GetMapping("/top-scores")
    public ResponseEntity<List<LeaderboardEntryDTO>> getTopScores() {
        List<LeaderboardEntryDTO> response = topScoresService.getTopScores();
        return ResponseEntity.status(response == null? HttpStatus.NOT_FOUND : HttpStatus.OK).body(response);
    }
}
