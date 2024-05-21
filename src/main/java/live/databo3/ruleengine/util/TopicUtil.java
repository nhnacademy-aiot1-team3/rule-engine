package live.databo3.ruleengine.util;

import live.databo3.ruleengine.event.message.EventMessage;
import live.databo3.ruleengine.event.message.MessagePayload;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class TopicUtil {
    private static final Map<String, String> topicInitial = Map.of(
            "s", "site",
            "b", "branch",
            "d", "device",
            "p", "place",
            "e", "endpoint",
            "t", "type",
            "ph", "phase",
            "de", "description");


    private TopicUtil() {
    }

    public static HashMap<String, String> getTopicValue(EventMessage<String, MessagePayload> eventMessage) {
        HashMap<String, String> topicValue = new HashMap<>();
        StringBuilder topicPattern = new StringBuilder("//([^/]+)");
        topicInitial.forEach((key, value) -> {
            try {
                topicPattern.insert(1, key);
                Pattern pattern = Pattern.compile(topicPattern.toString());
                Matcher matcher = pattern.matcher(eventMessage.getTopic());
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
        topicValue.put("topic", eventMessage.getTopic());
        return topicValue;
    }

//    public static TopicDto getTopicDto(String topic) {
//
//    }

}
