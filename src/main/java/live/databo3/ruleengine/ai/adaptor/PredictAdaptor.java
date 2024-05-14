package live.databo3.ruleengine.ai.adaptor;

import live.databo3.ruleengine.ai.dto.AiRequest;
import live.databo3.ruleengine.ai.dto.AiResponse;
import org.springframework.stereotype.Component;

@Component
public interface PredictAdaptor {
    AiResponse predictTemp(AiRequest aiRequest);

    AiResponse predictElect(AiRequest aiRequest);
}
