package live.databo3.ruleengine.ai.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import live.databo3.ruleengine.ai.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * RedisSaveService의 구현체
 *
 * @author 박상진
 * @version 1.0.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RedisServiceImpl implements RedisService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

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

    @Override
    public String getPredictElectValue(String orgnzationName) {
        log.info("org : {}", orgnzationName);
        Map<String, Object> orgRedisList = objectMapper.convertValue(redisTemplate.opsForHash().entries("ai:" + orgnzationName), new TypeReference<>() {});
        log.info("orgRedisList : {}",orgRedisList.toString());
        return orgRedisList.get("predictElect").toString();
    }


}
