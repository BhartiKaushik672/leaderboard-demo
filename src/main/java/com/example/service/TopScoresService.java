package com.example.service;

import com.example.dto.LeaderboardEntryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.springframework.data.redis.connection.RedisZSetCommands;

@Service
public class TopScoresService {

    private final RedisConnectionFactory connectionFactory;

    @Autowired
    public TopScoresService(RedisConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public List<LeaderboardEntryDTO> getTopScores() {
        try (RedisConnection connection = connectionFactory.getConnection()) {
            if (connection == null) {
                throw new TopScoresRetrievalException("Failed to establish connection to Redis");
            }
            return retrieveLeaderboardEntries(connection);
        } catch (RedisConnectionFailureException e) {
            throw new TopScoresRetrievalException("Failed to retrieve top scores from Redis", e);
        } catch (DataAccessException e) {
            throw new TopScoresRetrievalException("Data access exception occurred while retrieving top scores", e);
        }
    }

    // Inside the retrieveLeaderboardEntries method
    private List<LeaderboardEntryDTO> retrieveLeaderboardEntries(RedisConnection connection) {
        try {
            byte[] key = "players-top-scores".getBytes(StandardCharsets.UTF_8);
            Set<RedisZSetCommands.Tuple> leaderboard = connection.zRevRangeWithScores(key, 0, 5);
            return mapToLeaderboardEntries(leaderboard);
        } catch (DataAccessException e) {
            throw new TopScoresRetrievalException("Error retrieving leaderboard entries", e);
        }
    }

    // Convert RedisZSetCommands.Tuple to ZSetOperations.TypedTuple<byte[]>
    private List<LeaderboardEntryDTO> mapToLeaderboardEntries(Set<RedisZSetCommands.Tuple> leaderboard) {
        List<LeaderboardEntryDTO> leaderboardEntries = new ArrayList<>();
        for (RedisZSetCommands.Tuple leaderboardEntry : leaderboard) {
            String username = new String(leaderboardEntry.getValue(), StandardCharsets.UTF_8);
            double score = leaderboardEntry.getScore();
            leaderboardEntries.add(new LeaderboardEntryDTO(username, (int) score));
        }
        return leaderboardEntries;
    }


    public static class TopScoresRetrievalException extends RuntimeException {
        public TopScoresRetrievalException(String message) {
            super(message);
        }

        public TopScoresRetrievalException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
