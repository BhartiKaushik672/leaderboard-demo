package com.example.db.repository;

import com.example.db.entities.PlayerScore;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerScoreRepository extends JpaRepository<PlayerScore, Long> {

    PlayerScore findPlayerScoreByPlayerName(String player);
}
