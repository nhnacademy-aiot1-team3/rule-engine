package live.databo3.ruleengine.ai.service.impl;

import live.databo3.ruleengine.ai.service.CalculateService;
import live.databo3.ruleengine.ai.dto.AiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
public class CalculateServiceImpl implements CalculateService {

    @Override
    public String meanTemp(AiResponse aiResponse) {
        double sum = 0.0;
        int count = 0;

        for (double value : aiResponse.getPrediction()) {
            sum += value;
            count++;
        }

        double mean = sum / count;

        return String.format("%.1f", mean);
    }

    @Override
    public String kwhElect(AiResponse aiResponse) {
        double wSum = 0.0;

        for (double value : aiResponse.getPrediction()) {
            wSum += value;
        }
        double oneDayW = wSum / (144 * 7);

        double kwh = (oneDayW * 16) / 1000;

        double electBill = (kwh * 7 * 114.7) + 7220;

        return String.format("%.0f", electBill);
    }
}
