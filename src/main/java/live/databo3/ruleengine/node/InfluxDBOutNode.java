package live.databo3.ruleengine.node;

import live.databo3.ruleengine.dto.InfluxDBNodeEnvironment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class InfluxDBOutNode extends Node{

    private final InfluxDBNodeEnvironment influxDBNodeEnvironment;
}
