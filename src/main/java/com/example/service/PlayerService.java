package com.example.service;

import com.example.commons.CommonUtil;
import lombok.SneakyThrows;
import com.example.db.entities.PlayerScore;
import com.example.kafka.ScoreProducer;
import com.example.mappers.PlayerScoresMapper;
import com.example.model.PlayerDetails;
import com.example.db.repository.PlayerScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PlayerService {
    @Autowired
    private PlayerScoreRepository playerScoreRepository;

    @Autowired
    private ScoreProducer scoreProducer;

    @Autowired
    private PlayerScoresMapper playerScoresMapper;

    @Autowired
    private CommonUtil common;

    public void addUser(String playerName, String country) {

        PlayerScore playerScore = playerScoresMapper.maptoPlayerSCore(PlayerDetails.builder().playerName(playerName).country(country).build());

        if (common.checkIfPlayerAlreadyExists(playerName)){
            throw new RuntimeException("Player name already exists");
        }

        playerScoreRepository.save(playerScore);
        scoreProducer.sendScore(playerName, 0);

    }

    @SneakyThrows
    public void submitScore(String playerName, int score) {
        if(!common.checkIfPlayerAlreadyExists(playerName)){
            throw new RuntimeException("Player does not exist. Please login first");
        }
        scoreProducer.sendScore(playerName, score);
    }

    public PlayerDetails getPlayerScore(String playerName) {
        PlayerScore playerScore = playerScoreRepository.findPlayerScoreByPlayerName(playerName);

        if (playerScore == null) {
            throw new RuntimeException("Player not found");
        }

        return PlayerDetails.builder().playerName(playerName).score(playerScore.getScore()).country(playerScore.getCountry()).build();
    }


}
