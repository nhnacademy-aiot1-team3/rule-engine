package live.databo3.ruleengine.event.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class DataPayloadDto {
    private Long time;
    private Double value;
}
