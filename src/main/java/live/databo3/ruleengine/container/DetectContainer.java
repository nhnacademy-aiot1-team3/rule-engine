package live.databo3.ruleengine.container;

import java.text.SimpleDateFormat;
import java.util.*;

import live.databo3.ruleengine.domain.SensorMean;
import live.databo3.ruleengine.dto.SensorLogDto;
import live.databo3.ruleengine.service.SensorLogService;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Component;

@Component
public class DetectContainer {
    private final Map<String, Map<String, SensorMean>> locationList;
    private final SensorLogService sensorLogService = new SensorLogService();

    private final List<SensorLogDto> sensorLogDtos = new ArrayList<>();


    public DetectContainer() {
        this.locationList = new HashMap<>();
    }

    public void addOrUpdateSensorMean(String location, String sensorType, double sensorValue, Message message) throws ParseException {
        locationList.computeIfAbsent(location, s -> new HashMap<>());
        Map<String, SensorMean> sensorMeanList = locationList.get(location);
        sensorMeanList.computeIfAbsent(sensorType, s -> new SensorMean(sensorType));
        SensorMean sensorMean = sensorMeanList.get(sensorType);
        double meanValue = sensorMean.getMeanValue();
        JSONObject object = (JSONObject) new JSONParser().parse(new String(message.getBody()));
        String sensorSn = message.getMessageProperties().getReceivedRoutingKey().split("\\.")[8];
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(Long.parseLong(object.get("time").toString())));

        if (meanValue == 0) {
            sensorMean.setMeanValue(sensorValue);

//            System.out.println("---- 위치 : " + location + " // 센서 : " + sensorType + " // 평균값 : " + sensorMean.getMeanValue() + " 설정완료 ----");
        } else if (sensorType.length() < 3 || sensorType.equals("activity") || sensorType.equals("leq") || sensorType.equals("lmax") || sensorType.equals("spl")) {
            // 의미없는 센서 처리
        } else {
            double threshold = meanValue * 0.3;
            if (Math.abs(sensorValue - meanValue) > threshold) {
                // 이상 데이터 처리
                // 이상데이터 단건 insert (sersor_sn, timestamp, value)
                sensorLogService.insertErrorData(sensorSn,sensorType, timeStamp, sensorValue, meanValue);
            } else {
//                System.out.println("위치 : " + location + "[정상 데이터 통과 " + sensorType + ": " + sensorValue + " ---> meanValue : " + meanValue + "]");
                // 정상 데이터 처리
                double newMeanValue = (sensorValue + meanValue) / 2;
                sensorMean.setMeanValue(newMeanValue);
                sensorLogDtos.add(new SensorLogDto(sensorSn, sensorType, timeStamp, sensorValue));
                // 정상데이터 100개씩 bulk insert(sersor_sn, timestamp, value)
                if (sensorLogDtos.size() >= 100) {
                    sensorLogService.bulkInsertNormalData(sensorLogDtos);
                    sensorLogDtos.clear();
                }

            }
        }
    }
}