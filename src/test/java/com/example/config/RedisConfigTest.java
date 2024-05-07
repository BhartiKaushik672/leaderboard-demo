package com.example.config;

import com.example.Main;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = Main.class)
@TestPropertySource(properties = {
        "spring.redis.host=localhost",
        "spring.redis.port=6379"
})
class RedisConfigTest {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    void testRedisTemplateBeanCreation() {
        assertNotNull(redisTemplate);
    }

    @Test
    void testValueSerializer() {
        assertNotNull(redisTemplate.getValueSerializer());
        assertEquals(StringRedisSerializer.class, redisTemplate.getValueSerializer().getClass());
    }

    @Test
    void testKeySerializer() {
        assertNotNull(redisTemplate.getKeySerializer());
        assertEquals(StringRedisSerializer.class, redisTemplate.getKeySerializer().getClass());
    }
}
