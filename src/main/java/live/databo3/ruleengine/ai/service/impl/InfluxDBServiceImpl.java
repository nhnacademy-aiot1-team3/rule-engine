package live.databo3.ruleengine.ai.service.impl;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.QueryApi;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import live.databo3.ruleengine.ai.service.InfluxDBService;
import live.databo3.ruleengine.ai.dto.AiRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * InfluxDBservice의 구현체
 *
 * @author 박상진
 * @version 1.0.0
 */
@Service
@RequiredArgsConstructor
public class InfluxDBServiceImpl implements InfluxDBService {

    private final InfluxDBClient influxDBClient;

    /**
     * {@inheritDoc}
     * 쿼리문을 통해서 데이터를 요청한 뒤 결과를 반환 하는 메서드
     *
     * @param fluxQuery influxdb 쿼리문
     * @return AiRquest 예측한 value와 date로 값이 할당됨
     * @since 1.0.0
     */
    @Override
    public AiRequest queryData(String fluxQuery) {
        QueryApi queryApi = influxDBClient.getQueryApi();

        List<FluxTable> tables = queryApi.query(fluxQuery);

        return processData(tables);
    }

    /**
     * {@inheritDoc}
     * 날짜를 가공한 후 결과를 반환 하는 메서드
     *
     * @param fluxTables influxdb에서 받은 데이터 리스트
     * @return AiRequest 예측한 value와 가공된 date로 값이 할당됨
     * @since 1.0.0
     */
    @Override
    public AiRequest processData(List<FluxTable> fluxTables) {
        List<FluxRecord> records = fluxTables.get(0).getRecords();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        AiRequest aiRequest = new AiRequest();
        List<String> dates = new ArrayList<>();
        List<Double> values = new ArrayList<>();

        for (FluxRecord fluxRecord : records) {
            String timeStr = String.valueOf(fluxRecord.getTime());
            timeStr = timeStr.replace('T', ' ').replace('Z', ' ').trim();

            if (timeStr.contains(".")) {
                timeStr = timeStr.substring(0, timeStr.indexOf("."));
            }

            LocalDateTime time = LocalDateTime.parse(timeStr, formatter);

            if (records.size() > 24) {
                time = time.plusDays(7);
            }
            else {
                time = time.plusDays(1);
            }

            Double value = (Double) fluxRecord.getValue();
            timeStr = time.format(formatter);

            dates.add(timeStr);
            values.add(value);
        }

        aiRequest.setDates(dates);
        aiRequest.setValues(values);

        return aiRequest;
    }
}
