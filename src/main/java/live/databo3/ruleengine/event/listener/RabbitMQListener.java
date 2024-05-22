package live.databo3.ruleengine.event.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import live.databo3.ruleengine.event.message.MessagePayload;
import live.databo3.ruleengine.event.message.RuleEngineEvent;
import live.databo3.ruleengine.event.message.EventMessage;
import live.databo3.ruleengine.flag.FromRabbitMQ;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitMQListener {
    private final ObjectMapper objectMapper;
    private final ApplicationEventPublisher publisher;
    private final RedisTemplate<String, Object> redisTemplate;

    @RabbitListener(queues = "${spring.rabbitmq.name}")
    public void onMessage(Message message) throws JsonProcessingException {
        try {
            String payload = new String(message.getBody());
            String topic = message.getMessageProperties().getReceivedRoutingKey().replace(".", "/");
            MessagePayload messagePayload = objectMapper.readValue(payload, MessagePayload.class);

            if (Objects.nonNull(messagePayload.getTime()) && Objects.nonNull(messagePayload.getValue())) {
                EventMessage<String,MessagePayload> eventMessage = new EventMessage<>(topic, messagePayload);

                publisher.publishEvent(new RuleEngineEvent<>(this, eventMessage, new FromRabbitMQ()));
            }


        } catch (MismatchedInputException e) {
            log.error("error : {}", e.getMessage());
        } catch (NullPointerException e) {
            log.error("Null : {} - Message : {}", e.getMessage(),new String(message.getBody()));
        }
    }


}
