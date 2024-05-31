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
     *
     * @param organzationName 회사명
     * @param hashKey predictTemp, preidctElect 중에 하나
     * @param value preidct 된 값
     * @since 1.0.0
     */
    void saveRedisWithOrganuzationName(String organzationName,String hashKey, Object value);

    /**
     * Redis에서 회사명을 통해서 predictElect의 값을 가져오는 메서드
     *
     * @param orgnzationName 회사명
     * @return predictElectValue 예측한 전기요금
     * @since 1.0.0
     */
    String getPredictElectValue(String orgnzationName);

}
