package live.databo3.ruleengine.node;

import live.databo3.ruleengine.wire.Wire;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Slf4j
@Component
public class NodeConnector {
    private Method inputConnectOutputWire;
    private Method outputConnectInputWire;
    private Method inputOutputConnectOutputWire;
    private Method inputOutputConnectInputWire;

    @PostConstruct
    public void init() {
        try {
            inputConnectOutputWire = InputNode.class.getDeclaredMethod("connectOutputWire", Wire.class);
            outputConnectInputWire = OutputNode.class.getDeclaredMethod("connectInputWire", Wire.class);
            inputOutputConnectOutputWire = InputOutputNode.class.getDeclaredMethod("connectOutputWire", Wire.class);
            inputOutputConnectInputWire = InputOutputNode.class.getDeclaredMethod("connectInputWire", Wire.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public void connect(Node inputNode, Node outputNode, Wire wire) {

        try {
            if (inputNode instanceof InputOutputNode) {
                inputOutputConnectOutputWire.invoke(inputNode, wire);
            }else {
                inputConnectOutputWire.invoke(inputNode, wire);
            }
            if(outputNode instanceof InputOutputNode) {
                inputOutputConnectInputWire.invoke(outputNode, wire);
            }else{
                outputConnectInputWire.invoke(outputNode, wire);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }

//        if (inputNode instanceof OutputNode) {
//            log.error("In : 옳바르지않은 노드타입");
//        }
//        if (outputNode instanceof InputNode) {
//            log.error("Out : 옳바르지않은 노드타입");
//        }
//        try {
//            if (inputNode instanceof InputNode) {
//                inputConnectOutputWire.invoke(inputNode, wire);
//            }
//        } catch (IllegalAccessException e) {
//            throw new RuntimeException(e);
//        } catch (InvocationTargetException e) {
//            throw new RuntimeException(e);
//        }
//
//
////        inputNode.connectOutputWire(wire);
////        outputNode.connectInputWires(wire);
//        if (outputNode instanceof InputOutputNode) {
//            ((InputOutputNode) outputNode).connectOutputWire(wire);
//        }
    }
}
