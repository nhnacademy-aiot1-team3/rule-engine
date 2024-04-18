package live.databo3.ruleengine.rabbitmq.properties;

import lombok.Getter;

import static live.databo3.ruleengine.rabbitmq.Main.properties;

@Getter
public class RabbitMQProperties {
    private String host;
    private String queueName;

    public RabbitMQProperties() {
        host = properties.getProperty("rabbitmq.host");
        queueName = properties.getProperty("rabbitmq.queue.name");
    }
}
