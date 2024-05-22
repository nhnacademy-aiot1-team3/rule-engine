package live.databo3.ruleengine.ai.adaptor.impl;

import live.databo3.ruleengine.ai.adaptor.OrganizationAdaptor;
import live.databo3.ruleengine.ai.adaptor.PredictAdaptor;
import live.databo3.ruleengine.ai.adaptor.PredictSaveAdaptor;
import live.databo3.ruleengine.ai.dto.OrganizationResponse;
import live.databo3.ruleengine.ai.service.RedisSaveService;
import live.databo3.ruleengine.ai.service.impl.CalculateServiceImpl;
import live.databo3.ruleengine.ai.service.impl.InfluxDBServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class PredictSaveAdaptorImpl implements PredictSaveAdaptor {
    private final PredictAdaptor predictAdaptor;
    private final OrganizationAdaptor organizationAdaptor;
    private final CalculateServiceImpl calculateService;
    private final InfluxDBServiceImpl influxDBService;
    private final RedisSaveService redisSaveService;

    @Scheduled(cron = "0 0 */1 * * *")
    public void predictTemp() {
        List<OrganizationResponse> orgList = organizationAdaptor.getOrganizations().getBody();

        if(orgList == null || orgList.isEmpty()) {
            throw new NullPointerException();
        }
        else {
            for (OrganizationResponse org : orgList) {
                String fluxQuery = "from(bucket: \"raw_data\")\n" +
                        "  |> range(start: -7d)\n" +
                        "  |> filter(fn: (r) => r[\"_measurement\"] == \"databo3\")\n" +
                        "  |> filter(fn: (r) => r[\"branch\"] == \""+org.getOrganizationName()+"\")\n" +
                        "  |> filter(fn: (r) => r[\"endpoint\"] == \"temperature\")\n" +
                        "  |> group(columns: [\"branch\"])\n" +
                        "  |> aggregateWindow(every: 1h, fn: mean, createEmpty: false)\n" +
                        "  |> yield(name: \"mean\")";
                String preidctTemp = calculateService.meanTemp(predictAdaptor.predictTemp(influxDBService.queryData(fluxQuery)));
                log.info("preidctTemp: {}", preidctTemp);

                redisSaveService.saveRedisWithOrganuzationName(org.getOrganizationName(), "predictTemp", preidctTemp);
            }
        }


    }


    @Scheduled(cron = "0 0 */1 * * *")
    public void predictElect() {
        List<OrganizationResponse> orgList = organizationAdaptor.getOrganizations().getBody();

        if(orgList == null || orgList.isEmpty()) {
            throw new NullPointerException();
        }
        else {
            for(OrganizationResponse org : orgList) {
                String fluxQuery = "from(bucket: \"raw_data\")\n" +
                        "  |> range(start: -7d)\n" +
                        "  |> filter(fn: (r) => r[\"_measurement\"] == \"databo3\")\n" +
                        "  |> filter(fn: (r) => r[\"branch\"] == \""+org.getOrganizationName()+"\")\n" +
                        "  |> filter(fn: (r) => r[\"endpoint\"] == \"electrical_energy\")\n" +
                        "  |> filter(fn: (r) => r[\"description\"] == \"w\")\n" +
                        "  |> filter(fn: (r) => r[\"phase\"] == \"total\")\n" +
                        "  |> group(columns: [\"branch\"])\n" +
                        "  |> aggregateWindow(every: 10m, fn: mean, createEmpty: false)\n" +
                        "  |> yield(name: \"mean\")";

                String predictElect = calculateService.kwhElect(predictAdaptor.predictElect(influxDBService.queryData(fluxQuery)));
                log.info("predictElect: {}", predictElect);

                redisSaveService.saveRedisWithOrganuzationName(org.getOrganizationName(), "predictElect", predictElect);
            }
        }



    }
}
