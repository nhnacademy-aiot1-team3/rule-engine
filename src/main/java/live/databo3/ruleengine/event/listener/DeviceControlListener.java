package live.databo3.ruleengine.event.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.influxdb.client.service.OrganizationsService;
import live.databo3.ruleengine.ai.adaptor.OrganizationAdaptor;
import live.databo3.ruleengine.event.message.MessagePayload;
import live.databo3.ruleengine.event.message.RuleEngineEvent;
import live.databo3.ruleengine.event.message.TopicDto;
import live.databo3.ruleengine.sensor.adaptor.SensorAdaptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Map;

/*
 Redis 데이터 정보는 설정한 타입대로 넣을 것이므로 타입체크관련 문제는 없음.
 */
@SuppressWarnings("unckecked")
@Slf4j
@Component
@RequiredArgsConstructor
public class DeviceControlListener {
    private final RedisTemplate<String, Object> redisTemplate;
    private final SensorAdaptor sensorAdaptor;
    private final ObjectMapper objectMapper;

    @Async
    @EventListener(condition = "#ruleEngineEvent.from instanceof T(live.databo3.ruleengine.flag.FromTopicSplit)")
    public void controlDevice(RuleEngineEvent<TopicDto, MessagePayload> ruleEngineEvent) throws JsonProcessingException {
        TopicDto topicDto = ruleEngineEvent.getMsg().getTopic();
        if (Boolean.FALSE.equals(redisTemplate.hasKey(topicDto.getBranch()))) {
            log.info(topicDto.getBranch());
            sensorAdaptor.reloadRedis(topicDto.getBranch());
        }
        if (topicDto.getBranch().equals("demo") && topicDto.getEndpoint().equals("co2")) {
            Map<String, String> organizationConfig = objectMapper.convertValue(redisTemplate.opsForHash().entries(topicDto.getBranch()), new TypeReference<>() {
            });
            log.info("organizationConfig : " + organizationConfig.toString());
            String redisConfig = organizationConfig.get("general:" + topicDto.getDevice() + "/" + topicDto.getEndpoint() + "/*");
            log.info("redisConfig : " + redisConfig);
            Map<String, String> config = objectMapper.readValue(redisConfig, Map.class);
            String configType = config.get("functionName");

            if (configType.equals("CUSTOM")) {
                String customRedisKey = organizationConfig.get("general:" + topicDto.getDevice() + "/" + topicDto.getEndpoint() + "/");
                Map<String, String> customConfig = objectMapper.readValue(customRedisKey, Map.class);
                Double target = Double.parseDouble(customConfig.get("target"));
                Double threshold = Double.parseDouble(customConfig.get("threshold"));
                if (Math.abs(ruleEngineEvent.getMsg().getPayload().getValue() - target) > threshold) {
                    log.info("수동 제어신호 전송");
                }else{
                    log.info("정상 데이터");
                }

            } else if (configType.equals("AI")) {
                Map<String, String> aiConfig = objectMapper.convertValue(redisTemplate.opsForHash().entries("ai:" + topicDto.getPlace()), new TypeReference<>() {
                });
                Double aiTarget = Double.parseDouble(aiConfig.get("predictTemp"));
                if (Math.abs(ruleEngineEvent.getMsg().getPayload().getValue() - aiTarget) > 1.5) {
                    log.info("AI 제어신호 전송");
                }else{
                    log.info("정상 데이터");
                }
            }
        }


    }

}
