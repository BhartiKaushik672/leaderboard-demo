package com.example.service;

import com.example.service.TopScoresService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;

import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TopScoresServiceTest {

    @Mock
    private RedisConnectionFactory connectionFactory;

    @Mock
    private RedisConnection redisConnection;

    @Mock
    private ZSetOperations<byte[], byte[]> zSetOperations;

    @Test
    void testGetTopScores_Successful() {
        TopScoresService service = new TopScoresService(connectionFactory);
        Set<TypedTuple<byte[]>> leaderboard = Collections.emptySet();
        when(connectionFactory.getConnection()).thenReturn(redisConnection);
        when(redisConnection.zRevRangeWithScores(any(byte[].class), anyLong(), anyLong())).thenAnswer(invocation -> leaderboard);

        assertNotNull(service.getTopScores());
        assertEquals(0, service.getTopScores().size());

        verify(connectionFactory, times(2)).getConnection();
        verify(redisConnection, times(2)).zRevRangeWithScores(any(byte[].class), anyLong(), anyLong());
    }

    @Test
    void testGetTopScores_ConnectionFailure() {
        TopScoresService service = new TopScoresService(connectionFactory);
        when(connectionFactory.getConnection()).thenThrow(RedisConnectionFailureException.class);

        assertThrows(TopScoresService.TopScoresRetrievalException.class, service::getTopScores);

        verify(connectionFactory).getConnection();
        verifyNoInteractions(redisConnection);
    }

}
