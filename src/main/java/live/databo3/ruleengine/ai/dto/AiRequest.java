package live.databo3.ruleengine.ai.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class AiRequest {
    private List<String> dates;
    private List<Double> values;
}
