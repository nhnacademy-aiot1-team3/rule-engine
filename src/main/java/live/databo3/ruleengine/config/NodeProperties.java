package live.databo3.ruleengine.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import live.databo3.ruleengine.dto.NodeEnvironment;
import live.databo3.ruleengine.dto.NodeSettingDto;
import live.databo3.ruleengine.dto.RabbitMQNodeEnvironment;
import live.databo3.ruleengine.node.Node;
import live.databo3.ruleengine.node.RabbitListenerNode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Slf4j
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "node-flow")
public class NodeProperties {

    private List<NodeSettingDto> setting;
    private Map<String,Map<String ,String>> environments;

    @Bean
    public Map<String, NodeEnvironment> nodeEnvironments() {
        return new HashMap<>();
    }

    @Bean
    public Map<Node,NodeEnvironment> nodeList() {
        return new HashMap<>();
    }

    @Bean
    public Map<Node, Node> nodeConnection() {
        return new HashMap<>();
    }


}
