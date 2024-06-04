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

/**
 * 오류 데이터 처리 및 이상감지신호를 보내는 클래스.
 * @author 박종빈
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ErrorDetectEventListener {
    private final ApplicationEventPublisher applicationEventPublisher;
    private final Map<String, Object> sensorRefValue;
    private final Map<String, Object> sensorRefDef;
    private final Map<String, Object> sensorErrorCount;
    private final SensorAdaptor sensorAdaptor;

    /**
     * 실시간 데이터를 기반으로 한 데이터별 평균값을 기준으로, 기준 대비 실시간 데이터의 value 값이 과도하게 높거나 낮은 경우 오류 감지
     * 오류 데이터가 일정 횟수 이상 반복해서 들어올 시 오류데이터가 아닌 이상환경 감지.
     * @param ruleEngineEvent Topic 분류 후 TopicSplit 으로부터 전달받은 이벤트 메시지.
     */
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
                    } else {
                        ErrorDto errorDto = new ErrorDto(topicDto.getDevice(), topicDto.getEndpoint(), eventMessage.getPayload().getValue(), "strange value received");
                        sensorAdaptor.errorLogInsert(errorDto);
                    }
                }else {
                    sensorErrorCount.computeIfPresent(targetTopic, (k, v) -> 0);
                    sensorRefValue.computeIfPresent(targetTopic, (k,v) -> ((Double)v + eventMessage.getPayload().getValue()) / 2);

                }
            }
        }
    }
}
