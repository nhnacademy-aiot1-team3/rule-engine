package live.databo3.ruleengine.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import live.databo3.ruleengine.service.ControlMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ControlMessageServiceImpl implements ControlMessageService {
    private final ObjectMapper objectMapper;
    private final IMqttClient client;

    @Override
    public void controlMessagePublish(String deviceId, String state) {
        try {
            if(!client.isConnected()){
                log.error("클라이언트가 연결되지 않았습니다. 재연결을 시도합니다.");
                client.reconnect();
            }
            Map<String, String> message = new HashMap<>();
            message.put("device_id", deviceId);
            message.put("state", state);
            log.info("Publishing message: " + message);

            MqttMessage mqttMessage = new MqttMessage(objectMapper.writeValueAsBytes(message));
            mqttMessage.setQos(0); // QoS 설정
            mqttMessage.setRetained(false);

            client.publish("control/s/nhnacademy/conrollmessage", mqttMessage);
        } catch (MqttException e) {
            log.error("client 연결 오류.");
        } catch (JsonProcessingException e) {
            log.error("Object Mapper 오류.");
        }
    }
}
