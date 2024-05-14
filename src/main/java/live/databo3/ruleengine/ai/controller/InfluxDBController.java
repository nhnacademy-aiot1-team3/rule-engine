package live.databo3.ruleengine.ai.controller;

import live.databo3.ruleengine.ai.dto.AiRequest;
import live.databo3.ruleengine.ai.service.InfluxDBService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class InfluxDBController {

    private final InfluxDBService influxDBService;

    @GetMapping("/tempdata")
    public AiRequest getTempData() {
        String fluxQuery = "from(bucket: \"raw_data\")\n" +
                "  |> range(start: -7d)\n" +
                "  |> filter(fn: (r) => r[\"_measurement\"] == \"nhnacademy\")\n" +
                "  |> filter(fn: (r) => r[\"branch\"] == \"gyeongnam\")\n" +
                "  |> filter(fn: (r) => r[\"endpoint\"] == \"temperature\")\n" +
                "  |> filter(fn: (r) => r[\"place\"] == \"class_a\" or r[\"place\"] == \"class_b\" or r[\"place\"] == \"lobby\" or r[\"place\"] == \"meeting_room\" or r[\"place\"] == \"office\" or r[\"place\"] == \"server_room\" or r[\"place\"] == \"storage\")\n" +
                "  |> group(columns: [\"site\"])\n" +
                "  |> aggregateWindow(every: 1h, fn: mean, createEmpty: false)\n" +
                "  |> yield(name: \"mean\")";

        return influxDBService.queryData(fluxQuery);
    }

    @GetMapping("/electdata")
    public AiRequest getElectData() {
        String fluxQuery = "from(bucket: \"raw_data\")\n" +
                "  |> range(start: -7d)\n" +
                "  |> filter(fn: (r) => r[\"_measurement\"] == \"nhnacademy\")\n" +
                "  |> filter(fn: (r) => r[\"branch\"] == \"gyeongnam\")\n" +
                "  |> filter(fn: (r) => r[\"endpoint\"] == \"electrical_energy\")\n" +
                "  |> filter(fn: (r) => r[\"description\"] == \"w\")\n" +
                "  |> filter(fn: (r) => r[\"phase\"] == \"total\")\n" +
                "  |> group(columns: [\"_measurement\", \"branch\", \"endpoint\", \"description\", \"phase\", \"site\"])\n" +
                "  |> aggregateWindow(every: 1h, fn: mean, createEmpty: false)\n" +
                "  |> yield(name: \"mean\")";

        return influxDBService.queryData(fluxQuery);
    }
}
