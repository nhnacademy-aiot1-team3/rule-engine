package live.databo3.ruleengine.dto;

import lombok.*;
import org.springframework.beans.factory.annotation.Value;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class NodeSettingDto implements Serializable {
    private String nodeName;
    private String nodeType;
    private String environmentName;
    private List<String> outputNodeList;
}
