package live.databo3.ruleengine.filter;

import live.databo3.ruleengine.container.DetectContainer;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.amqp.core.Message;

public class ErrorDataDetector {
    static DetectContainer detect = new DetectContainer();

    private ErrorDataDetector(){}

    public static void detectError(Message message) throws ParseException {
        String[] topicArr = message.getMessageProperties().getReceivedRoutingKey().split("\\.");
        JSONObject object = (JSONObject) new JSONParser().parse(new String(message.getBody()));
        String location = topicArr[6];
        String sensorType = topicArr[topicArr.length - 1];
        double value =  Double.parseDouble(object.get("value").toString());
        detect.addOrUpdateSensorMean(location, sensorType, value, message);
    }
}
