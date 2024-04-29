package live.databo3.ruleengine.sensordatalog.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "sensor_data_log")
public class SensorDataLog {
    @Id
    private Long logId;
    private String sensorSerialNumber;
    private String sensorType;
    private LocalDateTime timeStamp;
    private Double value;
}
