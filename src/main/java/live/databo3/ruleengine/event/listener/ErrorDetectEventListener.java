package live.databo3.ruleengine.event.listener;


import com.fasterxml.jackson.databind.ObjectMapper;
import live.databo3.ruleengine.event.dto.DataPayloadDto;
import live.databo3.ruleengine.event.dto.EventMessage;
import live.databo3.ruleengine.event.dto.MessageDto;
import live.databo3.ruleengine.event.dto.TopicDto;
import live.databo3.ruleengine.util.TopicUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class ErrorDetectEventListener {
    private final ApplicationEventPublisher applicationEventPublisher;
    private final Map<String, Object> sensorMeanValue;
    private final Map<String, Object> sensorErrorCount;

    @Async
    @EventListener(condition = "#eventMessage.from instanceof T(live.databo3.ruleengine.flag.FromDataProcessing)")
    public void dataProcessing(EventMessage<DataPayloadDto> eventMessage) {
        MessageDto<DataPayloadDto> messageDto = eventMessage.getMsg();

        HashMap<String, String> topicValue = TopicUtil.getTopicValue(messageDto);
        TopicDto topicDto = new ObjectMapper().convertValue(topicValue, TopicDto.class);

        if (!sensorMeanValue.containsKey(topicDto.getSite())){
            sensorMeanValue.put(topicDto.getSite(), topicDto.getDevice());
        }

        sensorMeanValue.get(topicDto.getSite());






//        StopWatchUtil.stop(eventMessage.getId());
//        applicationEventPublisher.publishEvent(new EventMessage<>(this, eventMessage.getId(),processedMessage, new FromDataProcessing()));

    }
}
