package live.databo3.ruleengine.event.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import feign.FeignException;
import live.databo3.ruleengine.event.message.EventMessage;
import live.databo3.ruleengine.event.message.MessagePayload;
import live.databo3.ruleengine.event.message.RuleEngineEvent;
import live.databo3.ruleengine.event.message.TopicDto;
import live.databo3.ruleengine.flag.FromSensorExist;
import live.databo3.ruleengine.sensor.adaptor.SensorAdaptor;
import live.databo3.ruleengine.service.OrganizationInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SensorExistEventListener {
    private final ApplicationEventPublisher applicationEventPublisher;
    private final OrganizationInfoService organizationInfoService;



    @Async
    @EventListener(condition = "#ruleEngineEvent.from instanceof T(live.databo3.ruleengine.flag.FromTopicSplit)")
    public void sensorExist(RuleEngineEvent<TopicDto, MessagePayload> ruleEngineEvent) {
        TopicDto topic = ruleEngineEvent.getMsg().getTopic();
        try {
            organizationInfoService.getSensorListAndAddSensor(topic);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (FeignException.FeignClientException.BadRequest e) {
            log.error("error meg : {} | error topic : {}", e.getMessage(), topic);

        }
        EventMessage<TopicDto, MessagePayload> newEventMessage = new EventMessage<>(topic,ruleEngineEvent.getMsg().getPayload());
        applicationEventPublisher.publishEvent(new RuleEngineEvent<>(this, newEventMessage, new FromSensorExist()));

    }
}
