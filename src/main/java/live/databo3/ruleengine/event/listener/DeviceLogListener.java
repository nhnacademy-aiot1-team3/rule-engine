package live.databo3.ruleengine.event.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import live.databo3.ruleengine.event.message.*;
import live.databo3.ruleengine.sensor.adaptor.SensorAdaptor;
import live.databo3.ruleengine.util.TopicUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * 제어신호를 통한 장치 제어 후, 제어 확인 메시지를 받아 제어로그 DB에 로그를 저장하는 클래스
 * @author 박종빈
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DeviceLogListener {
    private final SensorAdaptor sensorAdaptor;
    private final ObjectMapper objectMapper;

    /**
     *
     * @param message 장치제어 후 RabbitMQ control-message queue 에 전달된 장치제어 확인 메시지
     * @throws JsonProcessingException ObjectMapper 를 이용하여 Message 의 payload 를 읽어 올 때 생길 수 있는 오류
     */
    @RabbitListener(queues = "control-message")
    public void deviceLogInsert(Message message) throws JsonProcessingException {
        String topic = message.getMessageProperties().getReceivedRoutingKey();
        String payload = new String(message.getBody());
        String deviceId = topic.split("\\.")[8];
        MessagePayload messagePayload = objectMapper.readValue(payload, MessagePayload.class);
        DeviceLogDto deviceLogDto = new DeviceLogDto(deviceId, messagePayload.getValue());
        sensorAdaptor.deviceLogInsert(deviceLogDto);
    }
}
