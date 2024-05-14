//package live.databo3.ruleengine.ex;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Repository;
//
//import javax.transaction.Transactional;
//import java.sql.PreparedStatement;
//import java.sql.Timestamp;
//import java.util.List;
//
//@Repository
//@RequiredArgsConstructor
//public class TestDataRepository {
//    private final JdbcTemplate jdbcTemplate;
//
//    @Transactional
//    public void saveAll(List<TestData> testDataList) {
//        String sql = "INSERT INTO TestData (uuid, created_At) VALUES (?, ?)";
//        jdbcTemplate.batchUpdate(
//                sql,
//                testDataList,
//                testDataList.size(),
//                (PreparedStatement ps, TestData testData) -> {
//                    ps.setString(1, testData.getUuid());
//                    ps.setTimestamp(2, Timestamp.valueOf(testData.getCreatedAt()));
//                });
//
//    }
//}
