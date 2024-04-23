package live.databo3.ruleengine.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import live.databo3.ruleengine.dto.InfluxDBNodeEnvironment;
import live.databo3.ruleengine.dto.NodeEnvironment;
import live.databo3.ruleengine.dto.RabbitMQNodeEnvironment;
import live.databo3.ruleengine.node.Node;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class NodeConfig {
    private final NodeProperties nodeProperties;
    private final ObjectMapper objectMapper;
    private final NodeFactory nodeFactory;
    private final Map<String, NodeEnvironment> nodeEnvironments;
    private final Map<Node, NodeEnvironment> nodeList;

    @PostConstruct
    public void init() {
        nodeProperties.getEnvironments().forEach((key, value) -> {
            NodeEnvironment nodeEnvironment = convertNodeEnvironment(key,value);
            nodeEnvironments.put(nodeEnvironment.getEnvironmentName(), nodeEnvironment);
        });

        nodeProperties.getSetting().stream().forEach(nodeSettingDto -> {
            Node node = nodeFactory.createNode(nodeEnvironments.get(nodeSettingDto.getEnvironmentName()),nodeSettingDto.getNodeType());
            nodeList.put(node, nodeEnvironments.get(nodeSettingDto.getEnvironmentName()));
            node.process();
        });

    }


    public NodeEnvironment convertNodeEnvironment(String key,Map<String, String> nodeEnvironment) {
        log.info("{}", nodeEnvironment);
        if ("rabbitMQ-environment".equals(key)) {
            return objectMapper.convertValue(nodeEnvironment, RabbitMQNodeEnvironment.class);
        }else if ("influxDB-environment".equals(key)) {
            return objectMapper.convertValue(nodeEnvironment, InfluxDBNodeEnvironment.class);
        }
        return null;
    }



}
