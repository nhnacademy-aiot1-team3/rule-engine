package live.databo3.ruleengine.node;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import live.databo3.ruleengine.dto.RabbitMQNodeEnvironment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Slf4j
@RequiredArgsConstructor
public class RabbitListenerNode extends Node {

    private final RabbitMQNodeEnvironment rabbitMQNodeEnvironment;

    public void connect() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(rabbitMQNodeEnvironment.getRabbitHost());
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.basicConsume(rabbitMQNodeEnvironment.getRabbitQueue(), true, (cosumerTag, delivery) -> {
            String payload = new String(delivery.getBody());
            String topic = delivery.getEnvelope().getRoutingKey();
            log.info("{}", payload);
            log.info("{}", topic);
        }, consumerTag -> {

        });
    }

    @Override
    public void process() {
        try {
            connect();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}
