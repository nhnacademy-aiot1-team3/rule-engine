package live.databo3.ruleengine.event.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import live.databo3.ruleengine.event.message.MessagePayload;
import live.databo3.ruleengine.event.message.RuleEngineEvent;
import live.databo3.ruleengine.event.message.EventMessage;
import live.databo3.ruleengine.event.message.TopicDto;
import live.databo3.ruleengine.flag.FromRabbitMQ;
import live.databo3.ruleengine.flag.FromTopicSplit;
import live.databo3.ruleengine.util.TopicUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.HashMap;


/**
 * 토픽 문자열을 필드로 나눠서 DTO 만들어주는 클래스
 * @author 양현성
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TopicSplitEventListener {
    private final ApplicationEventPublisher applicationEventPublisher;
    private final ObjectMapper objectMapper;

    @Async
    @EventListener(condition = "#ruleEngineEvent.from instanceof T(live.databo3.ruleengine.flag.FromRabbitMQ)")
    public void topicSplit(RuleEngineEvent<String,MessagePayload> ruleEngineEvent) {
        EventMessage<String,MessagePayload> eventMessage = ruleEngineEvent.getMsg();
        HashMap<String, String> topicValue = TopicUtil.getTopicValue(eventMessage);


        TopicDto topicDto = objectMapper.convertValue(topicValue, TopicDto.class);

        EventMessage<TopicDto, MessagePayload> newEventMessage = new EventMessage<>(topicDto,eventMessage.getPayload());

        applicationEventPublisher.publishEvent(new RuleEngineEvent<>(this, newEventMessage, new FromTopicSplit()));
    }
}
