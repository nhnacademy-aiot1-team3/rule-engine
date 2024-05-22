package live.databo3.ruleengine.ai.adaptor;

import live.databo3.ruleengine.ai.dto.AiRequest;
import live.databo3.ruleengine.ai.dto.AiResponse;
import org.springframework.stereotype.Component;

/**
 * Ai에 예측을 요청하는 Adaptor
 *
 * @author 박상진
 * @version 1.0.0
 */
public interface PredictAdaptor {
    /**
     * 온도를 예측한 뒤, 결과를 반환하는 메서드
     *
     * @param aiRequest date와 value를 가진다
     * @return AiResponse preidction로 값이 할당됨
     * @since 1.0.0
     */
    AiResponse predictTemp(AiRequest aiRequest);

    /**
     * 전력을 예측한 뒤, 결과를 반환하는 메서드
     *
     * @param aiRequest date와 value를 가진다
     * @return AiResponse preidction로 값이 할당됨
     * @since 1.0.0
     */
    AiResponse predictElect(AiRequest aiRequest);
}
