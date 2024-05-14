package live.databo3.ruleengine.ai.service;

import com.influxdb.query.FluxTable;
import live.databo3.ruleengine.ai.dto.AiRequest;

import java.util.List;

/**
 *
 * @author 박상진
 * @version 1.0.0
 */
public interface InfluxDBService {
    AiRequest queryData(String fluxQuery);

    AiRequest processData(List<FluxTable> fluxTables);
}
