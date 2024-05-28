package live.databo3.ruleengine.event.message;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class ErrorDto {
    private String sensorSn;
    private String sensorType;
    private Double value;
    private String errorMsg;
}
