package live.databo3.ruleengine.util;

import live.databo3.ruleengine.event.dto.DataPayloadDto;
import live.databo3.ruleengine.event.dto.MessageDto;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TopicUtil {
    private static final Map<String, String> topicInitial = Map.of(
            "s", "site",
            "b", "branch",
            "d", "device",
            "p", "place",
            "e", "endpoint",
            "t", "type",
            "ph", "phase",
            "de", "description");



    public static HashMap<String, String> getTopicValue(MessageDto<DataPayloadDto> messageDto) {
        HashMap<String, String> topicValue = new HashMap<>();
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
        return topicValue;
    }

//    public static TopicDto getTopicDto(String topic) {
//
//    }

}
