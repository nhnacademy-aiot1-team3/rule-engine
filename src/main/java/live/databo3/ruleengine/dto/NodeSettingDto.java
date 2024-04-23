package live.databo3.ruleengine.dto;

import lombok.*;
import org.springframework.beans.factory.annotation.Value;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class NodeSettingDto {
    private String nodeName;
    private String nodeFlow;
    private String nodeType;
    private String environmentName;
}
