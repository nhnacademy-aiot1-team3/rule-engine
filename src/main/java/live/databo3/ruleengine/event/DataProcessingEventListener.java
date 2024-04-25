package live.databo3.ruleengine.event;

import live.databo3.ruleengine.dto.DataPayloadDto;
import live.databo3.ruleengine.dto.EventMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataProcessingEventListener {

    @Async
    @EventListener(condition = "#eventMessage.from instanceof T(live.databo3.ruleengine.flag.FromValueCheck)")
    public void dataProcessing(EventMessage<DataPayloadDto> eventMessage) {
        Instant now = Instant.now();
        Instant payloadTime = Instant.ofEpochMilli(eventMessage.getMsg().getPayload().getTime());

        Long timeDifference = Duration.between(payloadTime, now).toSeconds();
        log.info("\n현재시간 : {}\n센서데이터 시간 : {}\n시간차이 : {}\npayload : {}", now.toEpochMilli(), payloadTime.toEpochMilli(), timeDifference, eventMessage.getMsg().getPayload());
        log.info("DataProcessingEventListener processing event: {}", eventMessage);
    }

}
