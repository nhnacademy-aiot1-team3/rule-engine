package live.databo3.ruleengine.node;

import live.databo3.ruleengine.wire.Wire;

import java.util.HashSet;
import java.util.Set;

public abstract class OutputNode implements Node{

    private Set<Wire> inputWires;


    public OutputNode() {
        this.inputWires = new HashSet<>();
    }

    public void connectInputWire(Wire wire) {
        this.inputWires.add(wire);
    }

    public Set<Wire> getInputWires() {
        return inputWires;
    }
}
