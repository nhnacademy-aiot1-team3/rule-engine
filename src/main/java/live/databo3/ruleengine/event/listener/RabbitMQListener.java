package live.databo3.ruleengine.event.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import live.databo3.ruleengine.event.dto.DataPayloadDto;
import live.databo3.ruleengine.event.dto.EventMessage;
import live.databo3.ruleengine.event.dto.MessageDto;
import live.databo3.ruleengine.ex.RedisTestDto;
import live.databo3.ruleengine.flag.FromRabbitMQ;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitMQListener {
    private final ObjectMapper objectMapper;
    private final ApplicationEventPublisher publisher;
    private static Long id = 0L;
    private final RedisTemplate<String, Object> redisTemplate;

    @RabbitListener(queues = "${spring.rabbitmq.name}")
    public void onMessage(Message message) throws JsonProcessingException {
        try {
            String payload = new String(message.getBody());
            String topic = message.getMessageProperties().getReceivedRoutingKey().replace(".", "/");
            DataPayloadDto dataPayloadDto = objectMapper.readValue(payload, DataPayloadDto.class);

            if (Objects.nonNull(dataPayloadDto.getTime()) && Objects.nonNull(dataPayloadDto.getValue())) {
                MessageDto<DataPayloadDto> messageDto = new MessageDto<>(topic, dataPayloadDto);

//                StopWatchUtil.start(id);
                log.info("{}", messageDto);
                publisher.publishEvent(new EventMessage<>(this, id++, messageDto, new FromRabbitMQ()));

            }


        } catch (MismatchedInputException e) {
            log.error("error : {}", e.getMessage());
        } catch (NullPointerException e) {
            log.error("Null : {}", e.getMessage());
        }
    }


}
