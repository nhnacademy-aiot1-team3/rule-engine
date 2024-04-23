package live.databo3.ruleengine.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class DataPayloadDto {
    private Long time;
    private Long value;
}
