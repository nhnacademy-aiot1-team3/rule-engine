package live.databo3.ruleengine.ai.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

/**
 * RestTemplate 설정을 위한 클래스
 *
 * @author 박상진
 * @version 1.0.0
 */
@Configuration
public class RestTemplateConfig {
    /**
     * 연결과 읽기의 타임아웃이 10초로 설정한 RestTemplate Bean
     *
     * @param builder RestTemplate을 빌드하는데 사용되는 RestTemplateBuilder
     * @return RestTemplate 지정된 타임아웃을 가진 resttemplate 인스턴스
     * @since 1.0.0
     */
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .setConnectTimeout(Duration.ofSeconds(10L))
                .setReadTimeout(Duration.ofSeconds(10L))
                .build();
    }
}
