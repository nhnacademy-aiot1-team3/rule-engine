package live.databo3.ruleengine.event.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
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
import java.util.Objects;
import java.util.UUID;

/**
 * 실시간 데이터의 value 값을 확인하여, Redis 에 저장된 저장정보들을 불러와 값을 비교하고, 상황에 맞게 Device 제어 신호를 보내는 클래스
 * @author 박종빈
 */
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

    /**
     * 실시간 데이터의 Branch 를 이용하여 Redis 에 해당 회사의 센서 정보를 불러오고, sensorSN 을 이용하여 해당 센서의 설정 확인.
     * 해당 센서의 설정이 CUSTOM 일 경우 deviceName 을 이용하여 제어장치를 찾고, 해당 제어장치에 동작 신호를 전송.
     * @param ruleEngineEvent 센서 정보 확인 후 SensorExist 로부터 전달받은 이벤트 메시지.
     * @throws JsonProcessingException ObjectMapper 를 통해 Redis 로부터 받아온 value 값 JSON 으로 매핑시 발생하는 오류
     */
    @Async
    @EventListener(condition = "#ruleEngineEvent.from instanceof T(live.databo3.ruleengine.flag.FromSensorExist)")
    public void controlDevice(RuleEngineEvent<TopicDto, MessagePayload> ruleEngineEvent) {
        TopicDto topicDto = ruleEngineEvent.getMsg().getTopic();
        if (Boolean.FALSE.equals(redisTemplate.hasKey(topicDto.getBranch()))) {
            sensorAdaptor.reloadRedis(topicDto.getBranch());
        }
        if (topicDto.getEndpoint().equals("temperature") || topicDto.getEndpoint().equals("humidity") || topicDto.getEndpoint().equals("co2")) {
            try {
                Map<String, String> organizationConfig = objectMapper.convertValue(redisTemplate.opsForHash().entries(topicDto.getBranch()), new TypeReference<>() {
                });
                String redisConfig = organizationConfig.get("general:" + topicDto.getDevice() + "/" + topicDto.getEndpoint());
                Map<String, String> config = objectMapper.readValue(redisConfig, Map.class);
                String configType = config.get("functionName");

                /*
                redis 에 저장된 sensor 에 따라 functionName 을 확인하고, CUSTOM 일 경우 수동제어, AI 일 경우 AI 분기 if-els
                 */
                if (configType.equals("CUSTOM") && (config.get("deviceName") != null)) {
                    String customRedisKey = organizationConfig.get("value:" + topicDto.getDevice() + "/" + topicDto.getEndpoint());
                    Map<String, String> customConfig = objectMapper.readValue(customRedisKey, Map.class);
                    double target = Double.parseDouble(customConfig.get("firstEntry"));
                    double threshold = Double.parseDouble(customConfig.get("secondEntry"));
                    if (Math.abs(ruleEngineEvent.getMsg().getPayload().getValue() - target) > threshold) {
                        /*
                        제어신호 MQTTMessage 로 보내는 부분.
                         */
                        if (ruleEngineEvent.getMsg().getPayload().getValue() - target > 0) {
                            controlMessageService.controlMessagePublish(config.get("deviceName"), "0");
                        } else {
                            controlMessageService.controlMessagePublish(config.get("deviceName"), "1");
                        }
                    }
                } else if (configType.equals("AI") && (config.get("deviceName") != null)) {
                    Map<String, String> aiConfig = objectMapper.convertValue(redisTemplate.opsForHash().entries("ai:" + topicDto.getPlace()), new TypeReference<>() {
                    });
                    Double aiTarget = Double.parseDouble(aiConfig.get("predictTemp"));
                    if (Math.abs(ruleEngineEvent.getMsg().getPayload().getValue() - aiTarget) > 1.5) {
                        if (ruleEngineEvent.getMsg().getPayload().getValue() - aiTarget > 0) {
                            controlMessageService.controlMessagePublish(config.get("deviceName"), "0");
                        } else {
                            controlMessageService.controlMessagePublish(config.get("deviceName"), "1");
                        }
                    }
                }
            } catch (JsonMappingException e) {
                log.error("JsonMappingException occurred : Branch : {}", topicDto.getBranch());
            } catch (JsonProcessingException e) {
                log.error("JsonProcessingException occurred : Branch : {}", topicDto.getBranch());
            } catch (Exception e){
                log.error("해당 Branch({})의 value 값이 존재하지 않습니다.", topicDto.getBranch());
            }

        }

    }

}
