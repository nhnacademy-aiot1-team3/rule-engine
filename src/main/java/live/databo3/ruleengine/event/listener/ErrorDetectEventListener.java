package live.databo3.ruleengine.event.listener;


import com.fasterxml.jackson.databind.ObjectMapper;
import live.databo3.ruleengine.event.dto.DataPayloadDto;
import live.databo3.ruleengine.event.dto.EventMessage;
import live.databo3.ruleengine.event.dto.MessageDto;
import live.databo3.ruleengine.event.dto.TopicDto;
import live.databo3.ruleengine.flag.FromErrorDetect;
import live.databo3.ruleengine.flag.FromRabbitMQ;
import live.databo3.ruleengine.util.TopicUtil;
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

    /*
    각 Map 에 들어가는 데이터의 타입은 직접 제어하고 있으므로, 타입체크의 문제는 없음.
     */
    @SuppressWarnings("unchecked")
    @Async
    @EventListener(condition = "#eventMessage.from instanceof T(live.databo3.ruleengine.flag.FromValueCheck)")
    public void errorDetector(EventMessage<DataPayloadDto> eventMessage) {
        MessageDto<DataPayloadDto> messageDto = eventMessage.getMsg();
        HashMap<String, String> topicValue = TopicUtil.getTopicValue(messageDto);
        TopicDto topicDto = new ObjectMapper().convertValue(topicValue, TopicDto.class);
        if (topicDto.getEndpoint().equals("temperature") || topicDto.getEndpoint().equals("co2") || topicDto.getEndpoint().equals("humidity")){
            String targetTopic = topicDto.getDevice() + topicDto.getEndpoint();
            if (!sensorRefValue.containsKey(targetTopic)){
                sensorRefDef.computeIfAbsent(targetTopic, v -> new ArrayList<Double>());
                if (((List<?>)sensorRefDef.get(targetTopic)).size() < 3){
                    ((List<Double>)sensorRefDef.get(targetTopic)).add(Double.parseDouble(messageDto.getPayload().getValue().toString()));
                }else {
                    Double sum = 0.0;
                    for (Double data : (List<Double>)sensorRefDef.get(targetTopic)){
                        sum += data;
                    }
                    sensorRefValue.put(targetTopic, sum / 3);
                }
            }else {
                if((Double) sensorRefValue.get(targetTopic) * 2 < messageDto.getPayload().getValue()){
                    sensorErrorCount.computeIfAbsent(targetTopic, v -> 0);
                    sensorErrorCount.put(targetTopic, (int)sensorErrorCount.get(targetTopic) + 1);
                    if ((int)sensorErrorCount.get(targetTopic) > 2){
                        //이상탐지 Event publish
                        applicationEventPublisher.publishEvent(new EventMessage<>(this, eventMessage.getId(), messageDto, new FromErrorDetect()));
                    }

                    System.out.println("Errordata 발생  기준값 : " + sensorRefValue.get(targetTopic) + " --- 현재값 : " + messageDto.getPayload().getValue());
                }else {
                    sensorErrorCount.computeIfPresent(targetTopic, (k, v) -> 0);
                    sensorRefValue.computeIfPresent(targetTopic, (k,v) -> ( (Double)v + messageDto.getPayload().getValue()) / 2);
                    System.out.println("정상데이터 처리 기준값 : " + sensorRefValue.get(targetTopic) + " --- 현재값 : " + messageDto.getPayload().getValue());
                applicationEventPublisher.publishEvent(this);
                }
            }
        }


//        StopWatchUtil.stop(eventMessage.getId());
//        applicationEventPublisher.publishEvent(new EventMessage<>(this, eventMessage.getId(),processedMessage, new FromDataProcessing()));

    }
}
