package live.databo3.ruleengine.util;

import org.springframework.util.StopWatch;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;

public class StopWatchUtil {

    private static final HashMap<Long, StopWatch> stopWatchHashMap = new HashMap<>();


    public static Long measurement(Instant payloadTime) {
        Instant now = Instant.now();
        Long timeDifference = Duration.between(payloadTime, now).toSeconds();
        return timeDifference;
    }

    public static void start(Long id) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatchHashMap.put(id, stopWatch);
    }

    public static void stop(Long id){
        StopWatch stopWatch = stopWatchHashMap.get(id);
        stopWatch.stop();
        long totalTimeMillis = stopWatch.getTotalTimeMillis();
        System.out.println("total time : " + totalTimeMillis);
        stopWatchHashMap.remove(id);
    }

}
