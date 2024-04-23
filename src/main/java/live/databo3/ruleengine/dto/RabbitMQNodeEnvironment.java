package live.databo3.ruleengine.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class RabbitMQNodeEnvironment implements NodeEnvironment{
    private String environmentName;
    private String rabbitHost;
    private Integer rabbitPort;
    private String rabbitQueue;
}
