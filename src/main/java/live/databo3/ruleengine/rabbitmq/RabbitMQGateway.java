//package live.databo3.ruleengine.rabbitmq;
//
//import lombok.RequiredArgsConstructor;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.amqp.core.AmqpTemplate;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.integration.annotation.MessagingGateway;
//import org.springframework.integration.annotation.ServiceActivator;
//import org.springframework.integration.annotation.Transformer;
//import org.springframework.integration.config.EnableIntegration;
//import org.springframework.integration.support.MessageBuilder;
//import org.springframework.messaging.Message;
//import org.springframework.stereotype.Component;
//
//@Component
//@EnableIntegration // 이 부분을 추가합니다.
//public class RabbitMQGateway{
//
//    @Transformer(inputChannel = "rabbitMQChannel", outputChannel = "processorChannel")
//    public Message<String> transformMessage(Message<String> message) {
//        return MessageBuilder.withPayload(message.getPayload()).build();
//    }
//
//    @ServiceActivator(inputChannel = "processorChannel")
//    public void processMessage(String message) {
//        // Process message here
//        RabbitMQProcessorNode processorNode = new RabbitMQProcessorNode();
//        processorNode.processData(message);
//    }
//
//}