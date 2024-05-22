package live.databo3.ruleengine.ai.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Ai에 요청하는 Dto
 *
 * @author 박상진
 * @version 1.0.0
 */
@Getter
@Setter
@NoArgsConstructor
public class AiRequest {
    private List<String> dates;
    private List<Double> values;
}
