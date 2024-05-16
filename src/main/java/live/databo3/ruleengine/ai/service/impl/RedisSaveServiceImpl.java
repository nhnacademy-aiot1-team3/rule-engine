package live.databo3.ruleengine.ai.service.impl;

import live.databo3.ruleengine.ai.service.RedisSaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisSaveServiceImpl implements RedisSaveService {
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void saveRedisWithOrganuzationName(String organzationName,String hashKey, Object value) {
        redisTemplate.opsForHash().put("ai:" + organzationName, hashKey, value);
    }
}
