package live.databo3.ruleengine.event.listener;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import live.databo3.ruleengine.event.message.MessagePayload;
import live.databo3.ruleengine.event.message.RuleEngineEvent;
import live.databo3.ruleengine.event.message.EventMessage;
import live.databo3.ruleengine.util.TopicUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class InfluxDBEventListener {

    private final InfluxDBClient influxDBClient;

    private void write(Point point) {
        influxDBClient.getWriteApiBlocking().writePoint(point);
    }

    private Point makePoint(Map<String, String> topicValue, MessagePayload payload) {
        Point point = Point.measurement(topicValue.get("site"))
                .time(payload.getTime(), WritePrecision.MS);
        topicValue.forEach(point::addTag);
        point.addField("sensor_value", payload.getValue());
        return point;
    }

    @Async
    @EventListener(condition = "#ruleEngineEvent.from instanceof T(live.databo3.ruleengine.flag.FromRabbitMQ)")
    public void insertInflux(RuleEngineEvent<String,MessagePayload> ruleEngineEvent) {
        EventMessage<String,MessagePayload> eventMessage = ruleEngineEvent.getMsg();
        HashMap<String, String> topicValue = TopicUtil.getTopicValue(eventMessage);
        Point point = makePoint(topicValue, eventMessage.getPayload());
        write(point);
    }


}
