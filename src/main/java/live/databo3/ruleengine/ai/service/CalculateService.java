package live.databo3.ruleengine.ai.service;

import live.databo3.ruleengine.ai.dto.AiResponse;

/**
 * 예측한 값들을 통해서 계산하는 서비스
 *
 * @author 박상진
 * @version 1.0.0
 */
public interface CalculateService {
    /**
     * 일주일의 예측한 온도를 평균을 구하는 메서드
     *
     * @param aiResponse 예측한 값들
     * @return 평균 온도
     * @since 1.0.0
     */
    String meanTemp(AiResponse aiResponse);

    /**
     * 일주일의 예측한 전력을 통해 전기요금을 구하는 메서드
     *
     * @param aiResponse 예측한 값들
     * @return 전기요금
     * @since 1.0.0
     */
    String kwhElect(AiResponse aiResponse);
}
