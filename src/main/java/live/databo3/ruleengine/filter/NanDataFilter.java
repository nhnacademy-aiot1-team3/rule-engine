package live.databo3.ruleengine.filter;

import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.TypeMismatchException;
import org.springframework.stereotype.Component;

import static live.databo3.ruleengine.filter.ErrorDataDetector.detectError;

@Slf4j
@Component
public class NanDataFilter {
    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void payloadFilter(Message message){
        if (message != null){
            try {
                JSONObject payload = (JSONObject) new JSONParser().parse(new String(message.getBody()));
                if (message.getMessageProperties().getReceivedRoutingKey().contains("gems-3500")){
                    // 전력데이터 필터링 -- 추후 node-red단에서 처리할건지 결정.
                }else if ((payload.get("value") == null) && (message.getMessageProperties().getReceivedRoutingKey() != null)){
                    System.out.println("결측 데이터 입니다.(value = null) topic : " + message.getMessageProperties().getReceivedRoutingKey());
                    // to Modify Nod
                }else {
                    detectError(message);
                }
            } catch (ParseException e) {
                throw new RuntimeException(e);
            } catch (TypeMismatchException e){
                System.err.println(e);
            }
        }

    }
}