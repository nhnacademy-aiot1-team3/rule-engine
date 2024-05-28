package live.databo3.ruleengine.event.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.influxdb.client.service.OrganizationsService;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import live.databo3.ruleengine.ai.adaptor.OrganizationAdaptor;
import live.databo3.ruleengine.event.message.MessagePayload;
import live.databo3.ruleengine.event.message.RuleEngineEvent;
import live.databo3.ruleengine.event.message.TopicDto;
import live.databo3.ruleengine.sensor.adaptor.SensorAdaptor;
import live.databo3.ruleengine.service.ControlMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
    private final ControlMessageService controlMessageService;

    @Async
    @EventListener(condition = "#ruleEngineEvent.from instanceof T(live.databo3.ruleengine.flag.FromSensorExist)")
    public void controlDevice(RuleEngineEvent<TopicDto, MessagePayload> ruleEngineEvent) throws JsonProcessingException {
        TopicDto topicDto = ruleEngineEvent.getMsg().getTopic();
        if (Boolean.FALSE.equals(redisTemplate.hasKey(topicDto.getBranch()))) {
            log.info(topicDto.getBranch());
            sensorAdaptor.reloadRedis(topicDto.getBranch());
        }
        if (topicDto.getEndpoint().equals("temperature") || topicDto.getEndpoint().equals("humidity") || topicDto.getEndpoint().equals("co2")) {
            Map<String, String> organizationConfig = objectMapper.convertValue(redisTemplate.opsForHash().entries(topicDto.getBranch()), new TypeReference<>() {
            });
            String redisConfig = organizationConfig.get("general:" + topicDto.getDevice() + "/" + topicDto.getEndpoint());
            Map<String, String> config = objectMapper.readValue(redisConfig, Map.class);
            String configType = config.get("functionName");

            /*
            redis 에 저장된 sensor 에 따라 functionName 을 확인하고, CUSTOM 일 경우 수동제어, AI 일 경우 AI 분기 if-els
             */
            if (configType.equals("CUSTOM") && (!config.get("deviceName").equals("null"))) {
                String customRedisKey = organizationConfig.get("value:" + topicDto.getDevice() + "/" + topicDto.getEndpoint());
                log.info(customRedisKey);
                Map<String, String> customConfig = objectMapper.readValue(customRedisKey, Map.class);
                log.info("customConfig : " + customConfig);
                double target = Double.parseDouble(customConfig.get("firstEntry"));
                double threshold = Double.parseDouble(customConfig.get("secondEntry"));
                log.info("target : " + target + " // threshold : " + threshold);
                if (Math.abs(ruleEngineEvent.getMsg().getPayload().getValue() - target) > threshold) {
                    /*
                    제어신호 MQTTMessage 로 보내는 부분.
                     */
                    if (ruleEngineEvent.getMsg().getPayload().getValue() - target > 0) {
                        log.info("over 수동 제어신호 전송");
                        log.info(config.get("deviceName"));
                        controlMessageService.controlMessagePublish(config.get("deviceName"), "0");
                    } else {
                        log.info("under 수동 제어신호 전송");
                        controlMessageService.controlMessagePublish(config.get("deviceName"), "1");
                    }
                } else {
                    log.info("정상 데이터");
                }

            } else if (configType.equals("AI")) {
                Map<String, String> aiConfig = objectMapper.convertValue(redisTemplate.opsForHash().entries("ai:" + topicDto.getPlace()), new TypeReference<>() {
                });
                Double aiTarget = Double.parseDouble(aiConfig.get("predictTemp"));
                if (Math.abs(ruleEngineEvent.getMsg().getPayload().getValue() - aiTarget) > 1.5) {
                    if (ruleEngineEvent.getMsg().getPayload().getValue() - aiTarget > 0) {
                        log.info("over AI 제어신호 전송");
                        controlMessageService.controlMessagePublish(config.get("deviceName"), "0");
                    } else {
                        log.info("under AI 제어신호 전송");
                        controlMessageService.controlMessagePublish(config.get("deviceName"), "1");
                    }
                } else {
                    log.info("정상 데이터");
                }
            }
        }

    }

}
