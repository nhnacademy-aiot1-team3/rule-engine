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



@Component
@RequiredArgsConstructor
public class PredictAdaptorImpl implements PredictAdaptor {

    private final RestTemplate restTemplate;

    @Value("${predict.api.url}")
    String predictUrl;

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
