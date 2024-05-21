package live.databo3.ruleengine.event.message;

import live.databo3.ruleengine.flag.Flag;
import lombok.Getter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

@Getter
@ToString
public class RuleEngineEvent<T,K> extends ApplicationEvent {
    private final EventMessage<T,K> msg;
    private final Flag from;


    public RuleEngineEvent(Object source, EventMessage<T,K> msg, Flag from) {
        super(source);
        this.msg = msg;
        this.from = from;
    }
}
