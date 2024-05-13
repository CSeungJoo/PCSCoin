package kr.pah.pcs.pcscoin.global.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RedisTokenService {

    private final RedisTemplate<String, Object> redisTemplate;

    public void saveToken(UUID userId, String token) {
        redisTemplate.opsForValue().set(userId.toString(), token);
    }
    public String getToken(UUID userId) {
        return redisTemplate.opsForValue().get(userId).toString();
    }

    public boolean validateToken(UUID userId, String token) {
        String storedToken = (String) redisTemplate.opsForValue().get(userId.toString());
        return storedToken != null && storedToken.equals(token);
    }

    public void deleteToken(UUID userId) {
        redisTemplate.delete(userId.toString());
    }
}
