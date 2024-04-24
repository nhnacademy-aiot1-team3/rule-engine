package live.databo3.ruleengine.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import live.databo3.ruleengine.dto.InfluxDBNodeEnvironment;
import live.databo3.ruleengine.dto.NodeEnvironment;
import live.databo3.ruleengine.dto.RabbitMQNodeEnvironment;
import live.databo3.ruleengine.node.Node;
import live.databo3.ruleengine.node.NodeConnector;
import live.databo3.ruleengine.wire.BufferedWire;
import live.databo3.ruleengine.wire.Wire;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class NodeConfig {
    private final NodeConnector nodeConnector;
    private final NodeProperties nodeProperties;
    private final ObjectMapper objectMapper;
    private final NodeFactory nodeFactory;
    private final Map<String, NodeEnvironment> nodeEnvironments;
    private final Map<Node, NodeEnvironment> nodeEnvironmentMap;
    private Map<String, Node> nodeList;
    private Map<Node, List<Node>> connectionMap;

    @PostConstruct
    public void init() {
        nodeList = new HashMap<>();
        connectionMap = new HashMap<>();
        nodeProperties.getEnvironments().forEach((key, value) -> {
            NodeEnvironment nodeEnvironment = convertNodeEnvironment(key, value);
            nodeEnvironments.put(nodeEnvironment.getEnvironmentName(), nodeEnvironment);
        });


        nodeProperties.getSetting().forEach(nodeSettingDto -> {
            Node node = nodeFactory.createNode(nodeEnvironments.get(nodeSettingDto.getEnvironmentName()), nodeSettingDto.getNodeType());
            nodeList.put(nodeSettingDto.getNodeName(), node);
            if (Objects.nonNull(nodeSettingDto.getEnvironmentName())) {
                nodeEnvironmentMap.put(node, nodeEnvironments.get(nodeSettingDto.getEnvironmentName()));
            }
        });

        nodeProperties.getSetting().forEach(nodeSettingDto -> {
            Node node = nodeList.get(nodeSettingDto.getNodeName());
            if (Objects.nonNull(nodeSettingDto.getOutputNodeList())) {
                List<Node> outputNodeList = nodeSettingDto.getOutputNodeList().stream().filter(Objects::nonNull).map(s -> nodeList.get(s)).collect(Collectors.toList());
                connectionMap.put(node, outputNodeList);
            }
        });

        connectionMap.forEach((node, connectionList) -> {
            connectionList.forEach(nodeConnection -> {
                Wire wire = new BufferedWire();
                nodeConnector.connect(node, nodeConnection, wire);
            });
        });

        nodeList.values().forEach(Node::start);
    }


    public NodeEnvironment convertNodeEnvironment(String key, Map<String, String> nodeEnvironment) {
        if ("rabbitMQ-environment".equals(key)) {
            return objectMapper.convertValue(nodeEnvironment, RabbitMQNodeEnvironment.class);
        } else if ("influxDB-environment".equals(key)) {
            return objectMapper.convertValue(nodeEnvironment, InfluxDBNodeEnvironment.class);
        }
        return null;
    }


}
