package live.databo3.ruleengine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

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
