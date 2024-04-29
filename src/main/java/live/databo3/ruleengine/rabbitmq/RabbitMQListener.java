package live.databo3.ruleengine.rabbitmq;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQListener {

//    @RabbitListener(queues = "${rabbitmq.queue.name}")
//    public void receiveMessage(Message message) {
//        System.out.println(message);
//    }
}
