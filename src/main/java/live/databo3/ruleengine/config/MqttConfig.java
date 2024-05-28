package live.databo3.ruleengine.config;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Slf4j
@Configuration
public class MqttConfig {

    @Bean
    public IMqttClient mqttClient() throws MqttException {
        IMqttClient client = new MqttClient("tcp://192.168.71.92:1883", UUID.randomUUID().toString());
        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        options.setCleanSession(true);
        options.setUserName("user1");
        options.setPassword("1234".toCharArray());
        client.connect(options);
        return client;
    }
}
