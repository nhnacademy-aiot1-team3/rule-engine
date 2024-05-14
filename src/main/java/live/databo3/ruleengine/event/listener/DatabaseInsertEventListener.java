package live.databo3.ruleengine.event.listener;

import live.databo3.ruleengine.event.dto.EventMessage;
import live.databo3.ruleengine.event.dto.MessageDto;
import live.databo3.ruleengine.event.dto.TopicDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DatabaseInsertEventListener {

    @Async
    @EventListener(condition = "#eventMessage.from instanceof T(live.databo3.ruleengine.flag.FromDataProcessing)")
    public void dataProcessing(EventMessage<TopicDto> eventMessage) {
        MessageDto<TopicDto> messageDto = eventMessage.getMsg();

        log.info("{}", messageDto);
//        applicationEventPublisher.publishEvent(new EventMessage<>(this, processedMessage, new FromDataProcessing()));
    }


//    @Scheduled(fixedDelay = 1000L)
//    public void hello(){
//        log.info("{}","hello!!!");
//    }
}
