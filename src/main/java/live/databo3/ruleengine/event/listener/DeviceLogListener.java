package live.databo3.ruleengine.event.listener;

import live.databo3.ruleengine.event.message.*;
import live.databo3.ruleengine.sensor.adaptor.SensorAdaptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeviceLogListener {
    private final SensorAdaptor sensorAdaptor;

    @Async
    @EventListener(condition = "#ruleEngineEvent.from instanceof T(live.databo3.ruleengine.flag.FromTopicSplit)")
    public void sensorExist(RuleEngineEvent<TopicDto, MessagePayload> ruleEngineEvent) throws InterruptedException {
        TopicDto topic = ruleEngineEvent.getMsg().getTopic();
        if (topic.getEndpoint().equals("controller")){
            DeviceLogDto deviceLogDto = new DeviceLogDto(topic.getDevice(), ruleEngineEvent.getMsg().getPayload().getValue());
            sensorAdaptor.deviceLoginsert(deviceLogDto);
        }

    }

}
