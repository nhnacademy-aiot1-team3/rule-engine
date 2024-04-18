package live.databo3.ruleengine.rabbitmq.properties;

import lombok.Getter;

import static live.databo3.ruleengine.rabbitmq.Main.properties;

@Getter
public class InfluxDBProperties {
    private String url;
    private String org;
    private String bucket;
    private char[] token;

    public InfluxDBProperties() {
        this.url = properties.getProperty("influxdb.url");
        this.org = properties.getProperty("influxdb.org");
        this.bucket = properties.getProperty("influxdb.bucket");
        this.token = properties.getProperty("influxdb.token").toCharArray();
    }
}
