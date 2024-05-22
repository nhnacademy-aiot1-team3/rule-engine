package live.databo3.ruleengine.ai.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

/**
 * Spring Scheduler를 위한 설정 클래스
 * Scheduler는 싱글 스레드로 동작되지만 설정을 통해 멀티 스레드형식으로 사용할 수 있다
 *
 * @author 박상진
 * @version 1.0.0
 */
@Configuration
public class SchedulerConfig implements SchedulingConfigurer {
    /**
     * 스캐줄링 작업을 구성하는 메서드
     *
     * 사용자 정의 {@link ThreadPoolTaskScheduler}를 생성하고, 사용 가능한 프로세서 수에 1을 더한 크기로 스레드 풀을 설정
     * 그런 다음, 스케줄러를 초기화하고 {@link ScheduledTaskRegistrar}에 설정
     *
     * @param taskRegistrar 스캐줄링 작업을 등록하는 데 사용되는 taskRegistrar
     * @since 1.0.0
     */
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();

        int n = Runtime.getRuntime().availableProcessors();
        threadPoolTaskScheduler.setPoolSize(n + 1);
        threadPoolTaskScheduler.initialize();

        taskRegistrar.setTaskScheduler(threadPoolTaskScheduler);
    }
}
