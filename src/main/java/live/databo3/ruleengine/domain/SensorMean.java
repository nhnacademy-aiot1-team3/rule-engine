package live.databo3.ruleengine.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SensorMean {
    private String sensorType;
    private double meanValue;

    public SensorMean(String sensorType) {
        this.sensorType = sensorType;
        this.meanValue = 0;
    }
}
