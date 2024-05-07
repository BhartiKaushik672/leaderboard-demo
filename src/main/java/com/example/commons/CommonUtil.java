package com.example.commons;

import com.example.db.repository.PlayerScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommonUtil {

    @Autowired
    private PlayerScoreRepository playerScoreRepository;

    public boolean checkIfPlayerAlreadyExists(String playerName){
        return playerScoreRepository.findPlayerScoreByPlayerName(playerName) != null;
    }
}
