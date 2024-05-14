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
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataProcessingEventListener {
    private final ApplicationEventPublisher applicationEventPublisher;


    @Async
    @EventListener(condition = "#eventMessage.from instanceof T(live.databo3.ruleengine.flag.FromValueCheck)")
    public void dataProcessing(EventMessage<DataPayloadDto> eventMessage) {
        MessageDto<DataPayloadDto> messageDto = eventMessage.getMsg();

        HashMap<String, String> topicValue = TopicUtil.getTopicValue(messageDto);
        TopicDto topicDto = new ObjectMapper().convertValue(topicValue, TopicDto.class);

        MessageDto<TopicDto> processedMessage = new MessageDto<>(messageDto.getTopic(), topicDto);

//        StopWatchUtil.stop(eventMessage.getId());
//        applicationEventPublisher.publishEvent(new EventMessage<>(this, eventMessage.getId(),processedMessage, new FromDataProcessing()));

    }

}
