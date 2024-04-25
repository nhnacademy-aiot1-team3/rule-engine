package live.databo3.ruleengine;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import live.databo3.ruleengine.dto.DataPayloadDto;
import live.databo3.ruleengine.dto.EventMessage;
import live.databo3.ruleengine.dto.MessageDto;
import live.databo3.ruleengine.flag.Flag;
import live.databo3.ruleengine.flag.FromRabbitMQ;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitMQListener {
    private final ObjectMapper objectMapper;
    private final ApplicationEventPublisher publisher;

    @RabbitListener(queues = "${spring.rabbitmq.name}")
    public void onMessage(Message message) throws JsonProcessingException {
        try {
            String payload = new String(message.getBody());
            String topic = message.getMessageProperties().getReceivedRoutingKey().replace(".", "/");
            DataPayloadDto dataPayloadDto = objectMapper.readValue(payload, DataPayloadDto.class);
            if (Objects.nonNull(dataPayloadDto.getTime()) && Objects.nonNull(dataPayloadDto.getValue())) {
                MessageDto<DataPayloadDto> messageDto = new MessageDto<>(topic, dataPayloadDto);
//                Instant now = Instant.now();
//                Instant payloadTime = Instant.ofEpochMilli(dataPayloadDto.getTime());
//
//                Long timeDifference = Duration.between(payloadTime, now).toSeconds();
//            log.info("\n현재시간 : {}\n센서데이터 시간 : {}\n시간차이 : {}\npayload : {}", now.toEpochMilli(), payloadTime.toEpochMilli(), timeDifference, dataPayloadDto);
                publisher.publishEvent(new EventMessage<>(this, messageDto,new FromRabbitMQ()));
            }


        } catch (MismatchedInputException e) {
            log.error("error : {}", e.getMessage());
        } catch (NullPointerException e) {
            log.error("Null : {}", e.getMessage());
        }
    }


}
