package live.databo3.ruleengine.dto;

import live.databo3.ruleengine.flag.Flag;
import live.databo3.ruleengine.flag.FromRabbitMQ;
import live.databo3.ruleengine.flag.FromValueCheck;
import lombok.Getter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

import java.io.Serializable;

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
