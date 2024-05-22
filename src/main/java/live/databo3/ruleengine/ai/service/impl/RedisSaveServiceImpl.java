package live.databo3.ruleengine.ai.service.impl;

import live.databo3.ruleengine.ai.service.RedisSaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * RedisSaveService의 구현체
 *
 * @author 박상진
 * @version 1.0.0
 */
@Service
@RequiredArgsConstructor
public class RedisSaveServiceImpl implements RedisSaveService {
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * {@inheritDoc}
     * Redis에 ai:회사명, hashKey, value를 저장하는 메서드
     * @param organzationName 회사명
     * @param hashKey predictTemp, preidctElect 중에 하나
     * @param value preidct 된 값
     * @since 1.0.0
     */
    @Override
    public void saveRedisWithOrganuzationName(String organzationName,String hashKey, Object value) {
        redisTemplate.opsForHash().put("ai:" + organzationName, hashKey, value);
    }
}
