package live.databo3.ruleengine.ai.service;

import live.databo3.ruleengine.ai.dto.AiResponse;

public interface CalculateService {
    String meanTemp(AiResponse aiResponse);

    String kwhElect(AiResponse aiResponse);
}
