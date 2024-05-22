package live.databo3.ruleengine.ai.service;

import com.influxdb.query.FluxTable;
import live.databo3.ruleengine.ai.dto.AiRequest;

import java.util.List;

/**
 * InfluxDB에 쿼리문을 실행하는 서비스
 *
 * @author 박상진
 * @version 1.0.0
 */
public interface InfluxDBService {
    /**
     * InfluxDB 쿼리를 실행해 데이터를 받아오는 메서드
     * @param fluxQuery influxdb 쿼리문
     * @return AiRequest
     * @since 1.0.0
     */
    AiRequest queryData(String fluxQuery);

    /**
     * 쿼리에서 받아 온 데이터를 가공하는 메서드
     * @param fluxTables influxdb에서 받은 데이터 리스트
     * @return AiRequest
     * @since 1.0.0
     */
    AiRequest processData(List<FluxTable> fluxTables);
}
