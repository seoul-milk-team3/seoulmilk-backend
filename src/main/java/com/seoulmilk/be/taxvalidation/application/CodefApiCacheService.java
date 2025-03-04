package com.seoulmilk.be.taxvalidation.application;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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
                JOB_INDEX.getParamName(), response.get(JOB_INDEX.getParamName()),
                THREAD_INDEX.getParamName(), response.get(THREAD_INDEX.getParamName()),
                JTI.getParamName(), response.get(JTI.getParamName()),
                TWO_WAY_TIMESTAMP.getParamName(), response.get(TWO_WAY_TIMESTAMP.getParamName())
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

    public Map<String, Object> getTwoWayInfo(String id) {
        Map<String, Object> info = hashOperations.entries(id);
        Map<String, Object> twoWayInfoResponse = new HashMap<>();
        twoWayInfoResponse.put(JOB_INDEX.getParamName(),
                Integer.parseInt((String) info.get(JOB_INDEX.getParamName())));

        twoWayInfoResponse.put(THREAD_INDEX.getParamName(),
                Integer.parseInt((String) info.get(THREAD_INDEX.getParamName())));

        twoWayInfoResponse.put(JTI.getParamName(),  info.get(JTI.getParamName()));

        twoWayInfoResponse.put(TWO_WAY_TIMESTAMP.getParamName(),
                Long.parseLong((String) info.get(TWO_WAY_TIMESTAMP.getParamName())));

        return twoWayInfoResponse;
    }

    public void removeTwoWayInfo(String codefId) {
        redisTemplate.delete(codefId);
    }
}
