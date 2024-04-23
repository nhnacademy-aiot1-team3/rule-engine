//package live.databo3.ruleengine.node.output;
//
//import com.influxdb.client.InfluxDBClient;
//import com.influxdb.client.InfluxDBClientFactory;
//import com.influxdb.client.domain.WritePrecision;
//import com.influxdb.client.write.Point;
//import live.databo3.ruleengine.properties.InfluxDBProperties;
//import live.databo3.ruleengine.dto.DataPayloadDto;
//import live.databo3.ruleengine.dto.MessageDto;
//import lombok.extern.slf4j.Slf4j;
//
//import java.time.Instant;
//import java.util.HashMap;
//import java.util.concurrent.Flow.Subscriber;
//import java.util.concurrent.Flow.Subscription;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//@Slf4j
//public class InfluxDBOutNode implements Subscriber<MessageDto<String, DataPayloadDto>> {
//
//    private Subscription subscription;
//    private final InfluxDBProperties influxDBProperties;
//    private InfluxDBClient influxDBClient;
//
//    private final HashMap<String, String> SHORT_WORD = new HashMap<>() {{
//        put("s", "site");
//        put("b", "branch");
//        put("d", "device");
//        put("p", "place");
//        put("e", "endpoint");
//        put("t", "type");
//        put("p", "phase");
//        put("de", "description");
//    }};
//
//    private StringBuilder pattern = new StringBuilder("/([^/]+)");
//
//    public InfluxDBOutNode() {
//        this.influxDBProperties = new InfluxDBProperties();
//        this.influxDBClient = InfluxDBClientFactory.create(
//                influxDBProperties.getUrl(),
//                influxDBProperties.getToken(),
//                influxDBProperties.getOrg(),
//                influxDBProperties.getBucket());
//    }
//
//
//    private void writeInfluxDB(Point point) {
//        influxDBClient.getWriteApiBlocking().writePoint(point);
//    }
//
//    @Override
//    public void onSubscribe(Subscription subscription) {
//        this.subscription = subscription;
//        subscription.request(1);
//    }
//
//    @Override
//    public void onNext(MessageDto<String, DataPayloadDto> item) {
//        try {
//            System.out.println("InfluxDBOutNode onNext : " + item);
//            System.out.println(Instant.now().toEpochMilli());
//            System.out.println();
//            Point point = Point.measurement("nhnacademy")
//                    .time(item.getPayload().getTime(), WritePrecision.MS);
//
//            SHORT_WORD.forEach((key, value) -> {
//                try {
//                    Pattern p = Pattern.compile("/" + key + pattern);
//                    Matcher matcher = p.matcher(item.getTopic());
//                    String tag = null;
//                    if (matcher.find()) {
//                        tag = matcher.group(1);
//                        point.addTag(value, tag);
//                    }
//                } catch (Exception e) {
//                    log.error(e.getMessage());
//                }
//            });
//
//            point.addField("sensor_value", item.getPayload().getValue());
//            writeInfluxDB(point);
//            subscription.request(1);
//        } catch (Exception e) {
//            onError(e);
//        }
//    }
//
//    @Override
//    public void onError(Throwable throwable) {
//        log.error("InfluxDBOutNode onError : " + throwable.getMessage());
//    }
//
//    @Override
//    public void onComplete() {
//        System.out.println("complete");
//        influxDBClient.close();
//    }
//}
