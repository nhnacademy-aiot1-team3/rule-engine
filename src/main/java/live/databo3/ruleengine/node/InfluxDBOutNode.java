package live.databo3.ruleengine.node;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import live.databo3.ruleengine.dto.DataPayloadDto;
import live.databo3.ruleengine.dto.InfluxDBNodeEnvironment;
import live.databo3.ruleengine.dto.MessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@RequiredArgsConstructor
public class InfluxDBOutNode extends OutputNode {

    private final InfluxDBNodeEnvironment influxDBNodeEnvironment;
    private InfluxDBClient influxDBClient;
    private Map<String, String> topicInitial;
    private StringBuilder topicPattern;

    @Override
    public void init() {
        this.influxDBClient = InfluxDBClientFactory.create(
                influxDBNodeEnvironment.getInfluxUrl(),
                influxDBNodeEnvironment.getInfluxToken().toCharArray(),
                influxDBNodeEnvironment.getInfluxOrg(),
                influxDBNodeEnvironment.getInfluxBucket());
        this.topicInitial= Map.of(
                "s", "site",
                "b", "branch",
                "d", "device",
                "p", "place",
                "e", "endpoint",
                "t", "type",
                "ph", "phase",
                "de", "description");
        this.topicPattern = new StringBuilder("//([^/]+)");
    }

    private void writeInfluxDB(Point point) {
        influxDBClient.getWriteApiBlocking().writePoint(point);
    }

    private Point makePoint(Map<String,String> topicValue,DataPayloadDto payload) {
        Point point = Point.measurement(topicValue.get("site"))
                .time(payload.getTime(), WritePrecision.MS);
        topicValue.forEach((key, value) -> {
            point.addTag(key, value);
        });

        point.addField("sensor_value", payload.getValue());

        return point;
    }

    @Override
    public void process() {
        init();
        getInputWires().forEach(wire -> {
            try {
                Map<String, String> topicValue = new HashMap<>();
                BlockingQueue<MessageDto> messageQueue = wire.get();
                MessageDto<String, DataPayloadDto> messageDto = messageQueue.take();
//                log.info("{} : {}", Instant.now().toEpochMilli(),messageDto);

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
                        log.error(e.getMessage());
                    }finally {
                        topicPattern.delete(1, key.length()+1);
                    }
                });
                Point point = makePoint(topicValue, messageDto.getPayload());
                writeInfluxDB(point);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void start() {
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        while (true) {
            process();
        }
    }
}
