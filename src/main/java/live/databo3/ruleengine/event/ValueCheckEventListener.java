package live.databo3.ruleengine.event;

import live.databo3.ruleengine.dto.DataPayloadDto;
import live.databo3.ruleengine.dto.EventMessage;
import live.databo3.ruleengine.dto.MessageDto;
import live.databo3.ruleengine.flag.FromValueCheck;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class ValueCheckEventListener {
    private final ApplicationEventPublisher applicationEventPublisher;

    @Async
    @EventListener(condition = "#eventMessage.from instanceof T(live.databo3.ruleengine.flag.FromRabbitMQ)")
    public void nullCheck(EventMessage<DataPayloadDto> eventMessage) {
        MessageDto<DataPayloadDto> messageDto = eventMessage.getMsg();
        if (Objects.nonNull(messageDto.getPayload().getValue())) {
//            log.info("{}", messageDto);
            applicationEventPublisher.publishEvent(new EventMessage<>(this, messageDto, new FromValueCheck()));

        }
    }

}
