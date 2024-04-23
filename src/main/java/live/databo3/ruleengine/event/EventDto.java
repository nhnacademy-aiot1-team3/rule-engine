package live.databo3.ruleengine.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class EventDto<T> {
    private String topic;
    private T payload;
}
