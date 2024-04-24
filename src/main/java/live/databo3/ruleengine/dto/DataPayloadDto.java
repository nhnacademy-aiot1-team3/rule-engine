package live.databo3.ruleengine.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@ToString
@NoArgsConstructor
public class DataPayloadDto implements Serializable {
    private Long time;
    private Long value;
}
