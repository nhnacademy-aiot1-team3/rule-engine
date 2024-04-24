package live.databo3.ruleengine.node;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import live.databo3.ruleengine.dto.DataPayloadDto;
import live.databo3.ruleengine.dto.MessageDto;
import live.databo3.ruleengine.dto.RabbitMQNodeEnvironment;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.concurrent.TimeoutException;

@Slf4j
public class RabbitListenerNode extends InputNode {

    private final RabbitMQNodeEnvironment rabbitMQNodeEnvironment;

    public RabbitListenerNode(RabbitMQNodeEnvironment rabbitMQNodeEnvironment) {
        super();
        this.rabbitMQNodeEnvironment = rabbitMQNodeEnvironment;
    }

    private ObjectMapper objectMapper;

    private void connect() throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(rabbitMQNodeEnvironment.getRabbitHost());
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        channel.basicConsume(rabbitMQNodeEnvironment.getRabbitQueue(), true, (consumerTag, delivery) -> {
            if(Objects.nonNull(delivery)){
                String payload = new String(delivery.getBody());
                String topic = delivery.getEnvelope().getRoutingKey().replaceAll("\\.","/");
                DataPayloadDto dataPayloadDto = objectMapper.readValue(payload, DataPayloadDto.class);
                MessageDto<String, DataPayloadDto> messageDto = new MessageDto<>(topic, dataPayloadDto);
                Instant now = Instant.now();
                Instant payloadTime = Instant.ofEpochMilli(dataPayloadDto.getTime());

                Long timeDifference = Duration.between(payloadTime, now).toSeconds();
                System.out.printf("시간 차이 %d\n",timeDifference);
//                log.info("시간차이 {} : {}",timeDifference,messageDto);
                output(messageDto);
            }

        }, consumerTag -> {
        });
    }

    @Override
    public void init() {
        try {
            log.info("init rabbitmq node");
            connect();
            objectMapper = new ObjectMapper();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void process() {
        init();
    }

    @Override
    public void start() {
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        process();
    }
}
