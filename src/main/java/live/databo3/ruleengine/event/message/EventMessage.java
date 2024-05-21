package live.databo3.ruleengine.event.message;

import lombok.*;

import java.io.Serializable;

@Getter
@ToString
@AllArgsConstructor
public class EventMessage<T,K> implements Serializable {
    private T topic;
    private K payload;
}
