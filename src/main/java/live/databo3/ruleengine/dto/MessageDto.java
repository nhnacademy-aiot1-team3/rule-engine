package live.databo3.ruleengine.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@ToString
@AllArgsConstructor
public class MessageDto<T,V> implements Serializable {
    private T topic;
    private V payload;
}
