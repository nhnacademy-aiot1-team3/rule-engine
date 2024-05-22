package live.databo3.ruleengine.ai.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Ai에서 예측값을 받는 Dto
 *
 * @author 박상진
 * @version 1.0.0
 */
@Getter
@NoArgsConstructor
public class AiResponse {
    private List<Double> prediction;
}
