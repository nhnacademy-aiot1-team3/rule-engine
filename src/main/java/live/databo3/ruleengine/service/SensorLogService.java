package live.databo3.ruleengine.service;

import live.databo3.ruleengine.dto.SensorLogDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
@Component
public class SensorLogService {

    private static final String JDBC_URL = "jdbc:mysql://133.186.244.96:3306/nhn_academy_14";
    private static final String USERNAME = "nhn_academy_14";
    private static final String PASSWORD = "wvSjRM8JGjjK]ZnJ";

    // 이상 데이터 삽입
    public void insertErrorData(String sensorSn, String sensorType, String timeStamp, double sensorValue, double meanValue) {
        String sql = "INSERT INTO ErrorLog (sensor_sn, sensor_type, timestamp, value, ref_value) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, sensorSn);
            ps.setString(2, sensorType);
            ps.setString(3, timeStamp);
            ps.setDouble(4, sensorValue);
            ps.setDouble(5, meanValue);

            ps.executeUpdate();
            System.out.println("오류 데이터 삽입");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 정상 데이터 일괄 삽입
    public void bulkInsertNormalData(List<SensorLogDto> sensorLogDtos) {
        String sql = "INSERT INTO SensorDataLog (sensor_sn, sensor_type, timestamp, value) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            conn.setAutoCommit(false);

            for (SensorLogDto sensorLogDto : sensorLogDtos) {
                ps.setString(1, sensorLogDto.getSensorSn());
                ps.setString(2, sensorLogDto.getSensorType());
                ps.setString(3, sensorLogDto.getTimeStamp());
                ps.setDouble(4, sensorLogDto.getValue());
                ps.addBatch();
            }

            ps.executeBatch(); // 일괄 삽입 실행
            conn.commit(); // 수동 커밋
            System.out.println("정상 데이터 일괄 삽입");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}