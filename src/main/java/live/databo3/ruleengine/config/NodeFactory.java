package live.databo3.ruleengine.config;

import live.databo3.ruleengine.dto.InfluxDBNodeEnvironment;
import live.databo3.ruleengine.dto.NodeEnvironment;
import live.databo3.ruleengine.dto.RabbitMQNodeEnvironment;
import live.databo3.ruleengine.node.*;
import org.springframework.stereotype.Component;

@Component
public class NodeFactory {


    public Node createNode(NodeEnvironment environment, String nodeType) {
        if ("RabbitMQ-in".equals(nodeType)) {
            return new RabbitListenerNode((RabbitMQNodeEnvironment) environment);
        } else if ("InfluxDB-out".equals(nodeType)) {
            return new InfluxDBOutNode((InfluxDBNodeEnvironment) environment);
        } else if ("Debug-out".equals(nodeType)) {
            return new DebugNode();
        } else if ("NullCheck-inout".equals(nodeType)) {
            return new NullCheckNode();
        }

        return null;
    }
}
