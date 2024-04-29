package live.databo3.ruleengine.event.dto;

import live.databo3.ruleengine.flag.Flag;
import lombok.Getter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

@Getter
@ToString
public class EventMessage<T> extends ApplicationEvent {
    private final MessageDto<T> msg;
    private final Flag from;


    public EventMessage(Object source, MessageDto<T> msg,Flag from) {
        super(source);
        this.msg = msg;
        this.from = from;
    }
}