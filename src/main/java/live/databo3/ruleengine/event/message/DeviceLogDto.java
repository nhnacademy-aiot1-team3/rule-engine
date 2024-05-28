package live.databo3.ruleengine.event.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class DeviceLogDto {
    private String deviceSn;
    private Double value;
}
