package live.databo3.ruleengine.event;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import live.databo3.ruleengine.dto.DataPayloadDto;
import live.databo3.ruleengine.dto.EventMessage;
import live.databo3.ruleengine.dto.MessageDto;
import live.databo3.ruleengine.flag.FromRabbitMQ;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
@RequiredArgsConstructor
public class InfluxDBEventListener {

    private final InfluxDBClient influxDBClient;
    private Map<String, String> topicInitial;

    @PostConstruct
    public void init() {
        this.topicInitial = Map.of(
                "s", "site",
                "b", "branch",
                "d", "device",
                "p", "place",
                "e", "endpoint",
                "t", "type",
                "ph", "phase",
                "de", "description");
    }

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
        Map<String, String> topicValue = new HashMap<>();
        MessageDto<DataPayloadDto> messageDto = eventMessage.getMsg();
        StringBuilder topicPattern = new StringBuilder("//([^/]+)");
        topicInitial.forEach((key, value) -> {
            try {
                topicPattern.insert(1, key);
                Pattern pattern = Pattern.compile(topicPattern.toString());
                Matcher matcher = pattern.matcher(messageDto.getTopic());
                String tag = null;
                if (matcher.find()) {
                    tag = matcher.group(1);
                    topicValue.put(value, tag);
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                topicPattern.delete(1, key.length() + 1);
            }
        });

        Point point = makePoint(topicValue, messageDto.getPayload());
        write(point);

//        Instant now = Instant.now();
//        Instant payloadTime = Instant.ofEpochMilli(messageDto.getPayload().getTime());
//
//        Long timeDifference = Duration.between(payloadTime, now).toSeconds();
//        log.info("\n현재시간 : {}\n센서데이터 시간 : {}\n시간차이 : {}\npayload : {}", now.toEpochMilli(), payloadTime.toEpochMilli(), timeDifference, messageDto.getPayload());
    }


}
