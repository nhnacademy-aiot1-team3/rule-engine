import com.rabbitmq.client.DeliverCallback;//package live.databo3.ruleengine.node.input;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
//import com.rabbitmq.client.Channel;
//import com.rabbitmq.client.Connection;
//import com.rabbitmq.client.ConnectionFactory;
//import com.rabbitmq.client.DeliverCallback;
//import live.databo3.ruleengine.dto.DataPayloadDto;
//import live.databo3.ruleengine.dto.MessageDto;
//import live.databo3.ruleengine.properties.RabbitMQProperties;
//import lombok.extern.slf4j.Slf4j;
//
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//import java.util.concurrent.SubmissionPublisher;
//import java.util.concurrent.TimeoutException;
//
//@Slf4j
//public class RabbitMQListenerNode extends SubmissionPublisher<MessageDto<String, DataPayloadDto>> {
//    private final ObjectMapper objectMapper;
//    private final RabbitMQProperties rabbitMQProperties;
//
//    public RabbitMQListenerNode() {
//        this.objectMapper = new ObjectMapper();
//        this.rabbitMQProperties = new RabbitMQProperties();
//    }
//
//    private Channel getConnectionChannel() throws IOException, TimeoutException {
//        ConnectionFactory factory = new ConnectionFactory();
//        factory.setHost(rabbitMQProperties.getHost());
//        Connection connection = factory.newConnection();
//        return connection.createChannel();
//    }
//
//    private void listen() throws IOException, TimeoutException {
//        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
//            String payload = new String(delivery.getBody(), StandardCharsets.UTF_8);
//            String topic = delivery.getEnvelope().getRoutingKey().replaceAll("\\.","/");
//            try {
//                DataPayloadDto dataPayloadDto = objectMapper.readValue(payload, DataPayloadDto.class);
//                log.info("{}", dataPayloadDto);
//
//                submit(
//                        new MessageDto<>(topic, dataPayloadDto)
//                );
//
//            } catch (UnrecognizedPropertyException e) {
//                log.error("Unrecognized property: {}", e.getMessage());
//            }
//
//        };
//        getConnectionChannel().basicConsume(rabbitMQProperties.getQueueName(), true, deliverCallback, consumerTag -> {
//        });
//    }
//
//    public void process() {
//        try {
//            listen();
//        } catch (IOException | TimeoutException e) {
//            log.error("error: {}", e.getMessage());
//        }
//    }
//}
