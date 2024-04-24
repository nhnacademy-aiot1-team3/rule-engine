package live.databo3.ruleengine.node;


import live.databo3.ruleengine.dto.MessageDto;
import live.databo3.ruleengine.wire.Wire;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Set;

@Slf4j
public abstract class InputNode implements Node {
    private Set<Wire> outputWires;

    public InputNode() {
        this.outputWires = new HashSet<>();
    }

    public void connectOutputWire(Wire wire) {
        outputWires.add(wire);
    }

    public void output(MessageDto messageDto) {
        outputWires.forEach(wire -> {
            try {
                wire.put(messageDto);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
