package live.databo3.ruleengine.event.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class TopicDto {
    private String site;
    private String branch;
    private String place;
    private String device;
    private String endpoint;
    private String type;
    private String phase;
    private String description;
}
