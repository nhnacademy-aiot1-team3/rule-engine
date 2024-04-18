package live.databo3.ruleengine.rabbitmq.dto;

import lombok.*;

@Getter
@ToString
@AllArgsConstructor
public class MessageDto<T,V>{
    private T topic;
    private V payload;
}
