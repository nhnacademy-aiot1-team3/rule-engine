package live.databo3.ruleengine.ai.adaptor.impl;

import live.databo3.ruleengine.ai.adaptor.OrganizationAdaptor;
import live.databo3.ruleengine.ai.adaptor.PredictAdaptor;
import live.databo3.ruleengine.ai.adaptor.PredictSaveAdaptor;
import live.databo3.ruleengine.ai.dto.OrganizationResponse;
import live.databo3.ruleengine.ai.service.RedisService;
import live.databo3.ruleengine.ai.service.impl.CalculateServiceImpl;
import live.databo3.ruleengine.ai.service.impl.InfluxDBServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * PredictSaveAdaptor의 구현체
 *
 * @author 박상진
 * @version 1.0.0
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class PredictSaveAdaptorImpl implements PredictSaveAdaptor {
    private final PredictAdaptor predictAdaptor;
    private final OrganizationAdaptor organizationAdaptor;
    private final CalculateServiceImpl calculateService;
    private final InfluxDBServiceImpl influxDBService;
    private final RedisService redisSaveService;

    /**
     * {@inheritDoc}
     * account-service에 회사명조회를 요청한 후 쿼리문을 통해 평균값 예측 후 Redis에 저장하는 메서드
     * orgList에는 있지만 influxDB의 branch와 맞지 않을 경우 log error로 남김
     *
     * @since 1.0.0
     */
    @Scheduled(cron = "0 0 */1 * * *")
    public void predictSaveTemp() {
        List<OrganizationResponse> orgList = organizationAdaptor.getOrganizations().getBody();

        if (orgList == null || orgList.isEmpty()) {
            throw new NullPointerException();
        } else {
            for (OrganizationResponse org : orgList) {
                String fluxQuery = "from(bucket: \"raw_data\")\n" +
                        "  |> range(start: -7d)\n" +
                        "  |> filter(fn: (r) => r[\"_measurement\"] == \"databo3\")\n" +
                        "  |> filter(fn: (r) => r[\"branch\"] == \"" + org.getOrganizationName() + "\")\n" +
                        "  |> filter(fn: (r) => r[\"endpoint\"] == \"temperature\")\n" +
                        "  |> group(columns: [\"branch\"])\n" +
                        "  |> aggregateWindow(every: 1h, fn: mean, createEmpty: false)\n" +
                        "  |> yield(name: \"mean\")";

                try {
                    String preidctTemp = calculateService.meanTemp(predictAdaptor.predictTemp(influxDBService.queryData(fluxQuery)));
                    log.info("{} preidctTemp: {}", org.getOrganizationName(), preidctTemp);

                    redisSaveService.saveRedisWithOrganuzationName(org.getOrganizationName(), "predictTemp", preidctTemp);
                }catch (IndexOutOfBoundsException e) {
                    log.error(org.getOrganizationName() + "의 predict 정보를 불러오지 못했습니다.");
                }
            }
        }


    }

    /**
     * {@inheritDoc}
     * account-service에 회사명조회를 요청한 후 쿼리문을 통해 전기요금 예측 후 Redis에 저장하는 메서드
     * orgList에는 있지만 influxDB의 branch와 맞지 않을 경우 log error로 남김
     *
     * @since 1.0.0
     */
    @Scheduled(cron = "0 * */1 * * *")
    public void predictSaveElect() {
        List<OrganizationResponse> orgList = organizationAdaptor.getOrganizations().getBody();

        if (orgList == null || orgList.isEmpty()) {
            throw new NullPointerException();
        } else {
            for (OrganizationResponse org : orgList) {
                String fluxQuery = "from(bucket: \"raw_data\")\n" +
                        "  |> range(start: -30d)\n" +
                        "  |> filter(fn: (r) => r[\"_measurement\"] == \"databo3\")\n" +
                        "  |> filter(fn: (r) => r[\"branch\"] == \"" + org.getOrganizationName() + "\")\n" +
                        "  |> filter(fn: (r) => r[\"endpoint\"] == \"electrical_energy\")\n" +
                        "  |> filter(fn: (r) => r[\"description\"] == \"w\")\n" +
                        "  |> filter(fn: (r) => r[\"phase\"] == \"total\")\n" +
                        "  |> group(columns: [\"branch\"])\n" +
                        "  |> aggregateWindow(every: 10m, fn: mean, createEmpty: false)\n" +
                        "  |> yield(name: \"mean\")";
                try {
                    String predictElect = calculateService.kwhElect(predictAdaptor.predictElect(influxDBService.queryData(fluxQuery)));
                    log.info("{} predictElect: {}", org.getOrganizationName(), predictElect);
                  
                    redisSaveService.saveRedisWithOrganuzationName(org.getOrganizationName(), "predictElect", predictElect);
                }catch (Exception e){
                    log.error(org.getOrganizationName() + "의 predict 정보를 불러오지 못했습니다.");
                }

            }
        }
    }
}
