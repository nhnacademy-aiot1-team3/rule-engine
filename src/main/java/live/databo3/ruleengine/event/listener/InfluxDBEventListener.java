package live.databo3.ruleengine.event.listener;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import live.databo3.ruleengine.event.dto.DataPayloadDto;
import live.databo3.ruleengine.event.dto.EventMessage;
import live.databo3.ruleengine.event.dto.MessageDto;
import live.databo3.ruleengine.util.TopicUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
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

    private Point makePoint(Map<String, String> topicValue, DataPayloadDto payload) {
        Point point = Point.measurement(topicValue.get("site"))
                .time(payload.getTime(), WritePrecision.MS);
        topicValue.forEach(point::addTag);

        point.addField("sensor_value", payload.getValue());

        return point;
    }

    @Async
    @EventListener(condition = "#eventMessage.from instanceof T(live.databo3.ruleengine.flag.FromRabbitMQ)")
    public void insertInflux(EventMessage<DataPayloadDto> eventMessage) {
        MessageDto<DataPayloadDto> messageDto = eventMessage.getMsg();
        HashMap<String, String> topicValue = TopicUtil.getTopicValue(messageDto);
        Point point = makePoint(topicValue, messageDto.getPayload());
        write(point);

        Instant now = Instant.now();
        Instant payloadTime = Instant.ofEpochMilli(messageDto.getPayload().getTime());

        Long timeDifference = Duration.between(payloadTime, now).toSeconds();
        log.info("\n현재시간 : {}\n센서데이터 시간 : {}\n시간차이 : {}\npayload : {}", now.toEpochMilli(), payloadTime.toEpochMilli(), timeDifference, messageDto.getPayload());
    }


}
