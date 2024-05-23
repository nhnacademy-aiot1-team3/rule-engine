package live.databo3.ruleengine.event.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import live.databo3.ruleengine.event.message.*;
import live.databo3.ruleengine.flag.FromRabbitMQ;
import live.databo3.ruleengine.sensor.adaptor.SensorAdaptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeviceLogListener {
    private final SensorAdaptor sensorAdaptor;
    private final ObjectMapper objectMapper;

    @RabbitListener(queues = "controllercheck")
    public void deviceLogInsert(Message message) throws JsonProcessingException {
        String topic = message.getMessageProperties().getReceivedRoutingKey();
        String payload = new String(message.getBody());
        String deviceId = topic.split("\\.")[8];
        MessagePayload messagePayload = objectMapper.readValue(payload, MessagePayload.class);
        DeviceLogDto deviceLogDto = new DeviceLogDto(deviceId, messagePayload.getValue());
        sensorAdaptor.deviceLogInsert(deviceLogDto);
    }
}
