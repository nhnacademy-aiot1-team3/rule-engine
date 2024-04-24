package live.databo3.ruleengine.node;

import live.databo3.ruleengine.dto.MessageDto;
import live.databo3.ruleengine.wire.Wire;

import java.util.HashSet;
import java.util.Set;

public abstract class InputOutputNode implements Node {
    private Set<Wire> inputWires;
    private Set<Wire> outputWires;

    public InputOutputNode() {
        this.inputWires = new HashSet<>();
        this.outputWires = new HashSet<>();
    }

    public void connectInputWire(Wire wire) {
        this.inputWires.add(wire);
    }

    public Set<Wire> getInputWires() {
        return inputWires;
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
