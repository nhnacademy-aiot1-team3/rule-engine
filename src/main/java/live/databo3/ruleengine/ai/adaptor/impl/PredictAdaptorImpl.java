package live.databo3.ruleengine.ai.adaptor.impl;

import live.databo3.ruleengine.ai.adaptor.PredictAdaptor;
import live.databo3.ruleengine.ai.dto.AiRequest;
import live.databo3.ruleengine.ai.dto.AiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * PredictAdaptor의 구현체
 *
 * @author 박상진
 * @version 1.0.0
 */
@Component
@RequiredArgsConstructor
public class PredictAdaptorImpl implements PredictAdaptor {

    private final RestTemplate restTemplate;

    @Value("${predict.api.url}")
    String predictUrl;

    /**
     * {@inheritDoc}
     * Resttemplate을 이용해 ai서버에 온도예측을 POST 요청한 후, 결과를 반환하는 메서드
     *
     * @param aiRequest date와 value를 가진다
     * @return AiResponse preidctions 온도 에측값이 할당됨
     * @since 1.0.0
     */
    @Override
    public AiResponse predictTemp(AiRequest aiRequest) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<AiRequest> request = new HttpEntity<>(aiRequest, httpHeaders);
        ResponseEntity<AiResponse> exchange = restTemplate.exchange(
                predictUrl + "/predict/temp",
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<AiResponse>() {}
        );

        return exchange.getBody();
    }

    /**
     * {@inheritDoc}
     * Resttemplate을 이용해 ai서버에 전력예측을 POST 요청한 후, 결과를 반환하는 메서드
     *
     * @param aiRequest date와 value를 가진다
     * @return AiResponse preidctions 전력 에측값이 할당됨
     * @since 1.0.0
     */
    @Override
    public AiResponse predictElect(AiRequest aiRequest) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<AiRequest> request = new HttpEntity<>(aiRequest, httpHeaders);
        ResponseEntity<AiResponse> exchange = restTemplate.exchange(
                predictUrl + "/predict/elect",
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<AiResponse>() {}
        );

        return exchange.getBody();
    }
}
