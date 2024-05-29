package live.databo3.ruleengine.ai.service.impl;

import live.databo3.ruleengine.ai.service.CalculateService;
import live.databo3.ruleengine.ai.dto.AiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * CalculateService의 구현체
 *
 * @author 박상진
 * @version 1.0.0
 */
@Service
@Slf4j
public class CalculateServiceImpl implements CalculateService {
    /**
     * {@inheritDoc}
     * 예측한 값들을 통해서 평균값을 구한 후, 결과를 반환하는 메서드
     *
     * @param aiResponse 예측한 값들
     * @return mean 평균 온도
     * @since 1.0.0
     */
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

    /**
     * {@inherutDoc}
     * 예측한 값들을 통해서 전기요금을 구한 후, 결과를 반환하는 메서드
     *
     * @param aiResponse 예측한 값들
     * @return electBill 전기요금
     * @since 1.0.0
     */
    @Override
    public String kwhElect(AiResponse aiResponse) {
        double wSum = 0.0;

        for (double value : aiResponse.getPrediction()) {
            wSum += value;
        }

        double oneHourW = wSum / (30 * 24);
        double kwh = (oneHourW * 16) / 1000;
        double electBill = (kwh * 30 * 145.7) + 7220;

        return String.format("%.0f", electBill);
    }
}
