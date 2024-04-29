package live.databo3.ruleengine.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public class SensorLogDto {
    private String sensorSn;
    private String timeStamp;
    private String sensorType;
    private double value;

    public SensorLogDto(String sensorSn, String sensorType, String timeStamp, double value){
        this.sensorSn = sensorSn;
        this.sensorType = sensorType;
        this.timeStamp = timeStamp;
        this.value = value;
    }
}

