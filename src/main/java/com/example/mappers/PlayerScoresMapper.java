package com.example.mappers;

import com.example.db.entities.PlayerScore;
import com.example.model.PlayerDetails;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class PlayerScoresMapper {

    public PlayerScore maptoPlayerSCore(PlayerDetails playerDetails){
        PlayerScore playerScore =  PlayerScore.builder()
                .score(0)
                .playerName(playerDetails.getPlayerName())
                .country(playerDetails.getCountry())
                .build();
        playerScore.setCreatedOn(LocalDateTime.now());
        playerScore.setLastUpdatedOn(LocalDateTime.now());
        return playerScore;
    }
}
