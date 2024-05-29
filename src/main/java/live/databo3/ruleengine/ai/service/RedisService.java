package live.databo3.ruleengine.ai.service;

/**
 * Redis에 저장하는 서비스
 *
 * @author 박상진
 * @version 1.0.0
 */
public interface RedisService {
    /**
     * Redis에 회사명, hashkey, value를 저장하는 메서드
     * @param organzationName 회사명
     * @param hashKey predictTemp, preidctElect 중에 하나
     * @param value preidct 된 값
     * @since 1.0.0
     */
    void saveRedisWithOrganuzationName(String organzationName,String hashKey, Object value);

    String getPredictElectValue(String orgnzationName);

}
