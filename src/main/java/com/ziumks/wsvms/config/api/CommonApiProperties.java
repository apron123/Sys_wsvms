package com.ziumks.wsvms.config.api;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * 공통 api 시스템 프로퍼티 값 주입
 *
 * @author  이상민
 * @since   2024.06.04 16:30
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Valid
@ConfigurationProperties(prefix = "common.api")
@Configuration
public class CommonApiProperties {

    private Bulk bulk;
    private BaDda baDda;
    private Widget widget;

    // 벌크 서버 관련 설정
    @Getter
    @Setter
    public static class Bulk {
        @NotBlank(message = "bulk url 을(를) 찾을 수 없습니다.")
        private String url;
    }

    // 시스템 모니터링 서버 관련 설정
    @Getter
    @Setter
    public static class BaDda {
        @NotBlank(message = "ba-dda url 을(를) 찾을 수 없습니다.")
        private String url;
    }

    // 위젯 서버 관련 설정
    @Getter
    @Setter
    public static class Widget {
        @NotBlank(message = "widget url 을(를) 찾을 수 없습니다.")
        private String url;
    }

}
