package live.databo3.ruleengine.config;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InfluxDBConfig {
    @Value("${spring.influxdb2.url}")
    private String url;

    @Value("${spring.influxdb2.token}")
    private String token;

    @Value("${spring.influxdb2.org}")
    private String org;

    @Value("${spring.influxdb2.bucket}")
    private String bucket;

    @Bean
    public InfluxDBClient influxDBClient() {
        return InfluxDBClientFactory.create(
                url,
                token.toCharArray(),
                org,
                bucket
        );
    }
}
