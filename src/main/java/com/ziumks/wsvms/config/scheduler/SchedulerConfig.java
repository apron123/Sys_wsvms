package com.ziumks.wsvms.config.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * 스케쥴러 스레드 설정
 *
 * @author  이상민
 * @since   2024.05.21 16:30
 */
@Slf4j
@EnableAsync
@EnableScheduling
@RequiredArgsConstructor
@Configuration
public class SchedulerConfig {

    private final SchedulerProperties schedulerProps;

    /**
     * ThreadPoolTaskScheduler를 생성하여 반환합니다.
     *
     * @return ThreadPoolTaskScheduler 객체
     */
    @Bean
    public ThreadPoolTaskScheduler taskScheduler() {

        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();

        // 스레드 풀 설정
        // 원하는 스레드 수로 설정
        taskScheduler.setPoolSize(schedulerProps.getPoolSize());
        // 원하는 스레드 prefix 명 설정
        taskScheduler.setThreadNamePrefix(schedulerProps.getThreadNamePrefix());

        log.info("#################################################");
        log.info("## ThreadPoolTaskScheduler Config ");
        log.info("taskScheduler PoolSize : {}",taskScheduler.getPoolSize());
        log.info("taskScheduler ThreadNamePrefix : {}",taskScheduler.getThreadNamePrefix());
        log.info("#################################################");

        // 다양한 스레드 풀 설정을 추가할 수 있음

        return taskScheduler;
    }

}
