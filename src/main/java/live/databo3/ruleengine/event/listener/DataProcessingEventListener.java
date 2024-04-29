package live.databo3.ruleengine.event.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import live.databo3.ruleengine.event.dto.DataPayloadDto;
import live.databo3.ruleengine.event.dto.EventMessage;
import live.databo3.ruleengine.event.dto.TopicDto;
import live.databo3.ruleengine.util.TopicUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataProcessingEventListener {
    private final ApplicationEventPublisher applicationEventPublisher;


//    @Async
//    @EventListener(condition = "#eventMessage.from instanceof T(live.databo3.ruleengine.flag.FromValueCheck)")
//    public void dataProcessing(EventMessage<DataPayloadDto> eventMessage) {
//        Instant now = Instant.now();
//        Instant payloadTime = Instant.ofEpochMilli(eventMessage.getMsg().getPayload().getTime());
//        Long timeDifference = Duration.between(payloadTime, now).toSeconds();
//        log.info("\n현재시간 : {}\n센서데이터 시간 : {}\n시간차이 : {}\npayload : {}", now.toEpochMilli(), payloadTime.toEpochMilli(), timeDifference, eventMessage.getMsg().getPayload());
//        log.info("DataProcessingEventListener processing event: {}", eventMessage);
//        HashMap<String, String> topicValue = TopicUtil.getTopicValue(eventMessage.getMsg());
//        TopicDto topicDto = new ObjectMapper().convertValue(topicValue, TopicDto.class);
//
//    }

}
