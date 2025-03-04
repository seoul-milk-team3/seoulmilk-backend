package com.seoulmilk.be.taxvalidation.application;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.seoulmilk.be.taxvalidation.infrastructure.constants.CodefParameter.*;

@Service
public class CodefApiCacheService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final HashOperations<String, String, Object> hashOperations;

    public CodefApiCacheService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.hashOperations = redisTemplate.opsForHash();
    }

    public void saveCodefResponse(String id, Map<String, Object> response) {
        Map<String, Object> data = Map.of(
                JOB_INDEX.name(), response.get(JOB_INDEX.getParamName()),
                THREAD_INDEX.name(), response.get(THREAD_INDEX.getParamName()),
                JTI.name(), response.get(JTI.getParamName()),
                TWO_WAY_TIMESTAMP.name(), response.get(TWO_WAY_TIMESTAMP.getParamName())
        );
        saveData(id, data);
    }

    private void saveData(String id, Map<String, Object> data) {
        Map<String, Object> convertedData = data.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().toString()
                ));
        hashOperations.putAll(id, convertedData);
        redisTemplate.expire(id, 5, TimeUnit.MINUTES);
    }
}
