package live.databo3.ruleengine.event.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@ToString
@AllArgsConstructor
public class MessageDto<T> implements Serializable {
    private String topic;
    private T payload;
}
