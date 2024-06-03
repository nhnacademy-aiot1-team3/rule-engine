package live.databo3.ruleengine.event.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import live.databo3.ruleengine.event.message.MessagePayload;
import live.databo3.ruleengine.event.message.RuleEngineEvent;
import live.databo3.ruleengine.event.message.TopicDto;
import live.databo3.ruleengine.service.ControlMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class AlarmListener {
    private final ControlMessageService controlMessageService;
    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, Object> redisTemplate;

    @Async
    @EventListener(condition = "#ruleEngineEvent.from instanceof T(live.databo3.ruleengine.flag.FromErrorDetect)")
    public void alarmMessagePublish(RuleEngineEvent<TopicDto, MessagePayload> ruleEngineEvent) throws JsonProcessingException {
        TopicDto topicDto = ruleEngineEvent.getMsg().getTopic();
        Map<String, String> organizationConfig = objectMapper.convertValue(redisTemplate.opsForHash().entries(topicDto.getBranch()), new TypeReference<>() {
        });
        String redisConfig = organizationConfig.get("general:" + topicDto.getDevice() + "/" + topicDto.getEndpoint());
        Map<String, String> config = objectMapper.readValue(redisConfig, Map.class);
        if (config.get("deviceName") != null){
            log.info("StrangeData alarmControl published");
            controlMessageService.controlMessagePublish(config.get("deviceName"), "DO_1", "1");
        }
    }
}
