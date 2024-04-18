//package live.databo3.ruleengine.rabbitmq;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.amqp.core.Message;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.integration.dsl.IntegrationFlow;
//import org.springframework.integration.dsl.IntegrationFlows;
//import org.springframework.stereotype.Component;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class RabbitMQListener {
//
//    private final RabbitMQGateway processorGateway;
//
//    @RabbitListener(queues = "${rabbitmq.queue.name}")
//    public void receiveMessage(Message message) {
//        String messageBody = new String(message.getBody());
//        log.info("Received message: {}", messageBody);
//
//        // Send message to processor node
//        processorGateway.processMessage(messageBody);
//    }
//}
