package live.databo3.ruleengine.ai.controller;

import live.databo3.ruleengine.ai.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ElectChargeController {
    private final RedisService redisService;

    @GetMapping("/{orgName}/electcharge")
    public String electCharge(@PathVariable String orgName) {
        return redisService.getPredictElectValue(orgName);
    }
}
