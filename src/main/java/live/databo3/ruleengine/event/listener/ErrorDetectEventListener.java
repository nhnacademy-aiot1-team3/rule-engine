package live.databo3.ruleengine.event.listener;

import live.databo3.ruleengine.event.message.*;
import live.databo3.ruleengine.flag.FromErrorDetect;
import live.databo3.ruleengine.sensor.adaptor.SensorAdaptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class ErrorDetectEventListener {
    private final ApplicationEventPublisher applicationEventPublisher;
    private final Map<String, Object> sensorRefValue;
    private final Map<String, Object> sensorRefDef;
    private final Map<String, Object> sensorErrorCount;
    private final SensorAdaptor sensorAdaptor;

    /*
    각 Map 에 들어가는 데이터의 타입은 직접 제어하고 있으므로, 타입체크의 문제는 없음.
     */
    @SuppressWarnings("unchecked")
    @Async
    @EventListener(condition = "#ruleEngineEvent.from instanceof T(live.databo3.ruleengine.flag.FromTopicSplit)")
    public void errorDetector(RuleEngineEvent<TopicDto, MessagePayload> ruleEngineEvent) {
        EventMessage<TopicDto,MessagePayload> eventMessage = ruleEngineEvent.getMsg();
        TopicDto topicDto = eventMessage.getTopic();
        if (topicDto.getEndpoint().equals("temperature") || topicDto.getEndpoint().equals("co2") || topicDto.getEndpoint().equals("humidity")){
            String targetTopic = topicDto.getDevice() + topicDto.getEndpoint();
            if (!sensorRefValue.containsKey(targetTopic)){
                sensorRefDef.computeIfAbsent(targetTopic, v -> new ArrayList<Double>());
                if (((List<?>)sensorRefDef.get(targetTopic)).size() < 3){
                    ((List<Double>)sensorRefDef.get(targetTopic)).add(Double.parseDouble(eventMessage.getPayload().getValue().toString()));
                }else {
                    Double sum = 0.0;
                    for (Double data : (List<Double>)sensorRefDef.get(targetTopic)){
                        sum += data;
                    }
                    sensorRefValue.put(targetTopic, sum / 3);
                }
            }else {
                double refValue = (Double) sensorRefValue.get(targetTopic) /2;
                if(Math.abs(eventMessage.getPayload().getValue() - (Double) sensorRefValue.get(targetTopic)) > refValue){
                    sensorErrorCount.putIfAbsent(targetTopic, 0);
                    sensorErrorCount.put(targetTopic, (int)sensorErrorCount.get(targetTopic) + 1);
                    if ((int)sensorErrorCount.get(targetTopic) > 2){
                        EventMessage<TopicDto, MessagePayload> newEventMessage = new EventMessage<>(topicDto,eventMessage.getPayload());
                        applicationEventPublisher.publishEvent(new RuleEngineEvent<>(this, newEventMessage, new FromErrorDetect()));
                    }
                    ErrorDto errorDto = new ErrorDto(topicDto.getDevice(), topicDto.getEndpoint(), eventMessage.getPayload().getValue(), "strange value received");
                    sensorAdaptor.errorLogInsert(errorDto);
                }else {
                    sensorErrorCount.computeIfPresent(targetTopic, (k, v) -> 0);
                    sensorRefValue.computeIfPresent(targetTopic, (k,v) -> ( (Double)v + eventMessage.getPayload().getValue()) / 2);
                applicationEventPublisher.publishEvent(this);
                }
            }
        }
    }
}
