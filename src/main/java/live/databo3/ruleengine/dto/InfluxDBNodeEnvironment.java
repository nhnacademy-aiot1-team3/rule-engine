package live.databo3.ruleengine.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class InfluxDBNodeEnvironment implements NodeEnvironment{
    private String environmentName;
    private String influxUrl;
    private String influxBucket;
    private String influxToken;
    private String influxOrg;
}
