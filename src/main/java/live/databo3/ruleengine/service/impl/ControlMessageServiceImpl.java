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

    @Override
    public void controlMessagePublish(String deviceId, String state) {
        UUID uid = UUID.randomUUID();
        try (IMqttClient serverClient = new MqttClient("tcp://192.168.71.92:1883", uid.toString())) {
            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);
            options.setUserName("user1");
            options.setPassword("1234".toCharArray());
            serverClient.connect(options);


            Map<String, String> message = new HashMap<>();
            message.put("device_id",deviceId);
            message.put("state",state);
            log.info(message.toString());
            serverClient.publish("data/s/nhnacademy/conrollmessage", new MqttMessage(objectMapper.writeValueAsBytes(message)));

        } catch (MqttException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            log.error("JsonProcessException occurred");
        }
    }
}
