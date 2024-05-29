package live.databo3.ruleengine.ai.controller;

import live.databo3.ruleengine.ai.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * 전기요금 보내주는 컨트롤러
 *
 * @author 박상진
 * @version 1.0.0
 */
@RestController
@RequiredArgsConstructor
public class ElectChargeController {
    private final RedisService redisService;

    /**
     * Get요청 시 orgName을 파라미터로 받아서 전기요금을 반환하는 메서드
     *
     * @param orgName
     * @return predictElectValue redis에 저장된 예측된 전기요금
     */
    @GetMapping("/{orgName}/electcharge")
    public String electCharge(@PathVariable String orgName) {
        return redisService.getPredictElectValue(orgName);
    }
}
