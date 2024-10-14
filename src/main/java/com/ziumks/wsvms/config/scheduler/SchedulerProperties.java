package com.ziumks.wsvms.config.scheduler;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.Valid;

/**
 * 스케쥴러 시스템 프로퍼티 값 주입
 *
 * @author  이상민
 * @since   2024.05.21 16:30
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Valid
@ConfigurationProperties(prefix = "spring.task.scheduling")
@Configuration
public class SchedulerProperties {

    // 스레드 풀 사이즈
    private int poolSize = 1;

    // 스레드 prefix 명
    private String threadNamePrefix = "scheduler-";

}
