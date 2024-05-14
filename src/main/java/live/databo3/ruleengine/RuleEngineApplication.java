package live.databo3.ruleengine;

import live.databo3.ruleengine.ex.TestData;
import live.databo3.ruleengine.ex.TestDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.List;

@EnableAsync
@EnableScheduling
@SpringBootApplication
@ConfigurationPropertiesScan
public class RuleEngineApplication /*implements CommandLineRunner*/ {



    public static void main(String[] args) {
        SpringApplication.run(RuleEngineApplication.class, args);
    }

//    @Autowired
//    TestDataRepository testDataRepository;

//    @Override
//    public void run(String... args) throws Exception {
//        StopWatch stopWatch = new StopWatch();
//        stopWatch.start();
//        List<TestData> testDataList = new ArrayList<>();
//        for (int i = 0; i < 100000; i++) {
//            testDataList.add(TestData.create());
//        }
//        testDataRepository.saveAll(testDataList);
//        stopWatch.stop();
//        long totalTimeMillis = stopWatch.getTotalTimeMillis();
//        System.out.println("total time : " + totalTimeMillis);
//    }
}
