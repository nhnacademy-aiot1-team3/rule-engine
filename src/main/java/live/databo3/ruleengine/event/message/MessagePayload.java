package live.databo3.ruleengine.event.message;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class MessagePayload {
    private Long time;
    private Double value;
}
