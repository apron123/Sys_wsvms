package com.ziumks.wsvms.config.api;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * svms api 시스템 프로퍼티 값 주입
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
@ConfigurationProperties(prefix = "svms")
@Configuration
public class SvmsApiProperties {

    private Websocket websocket;
    private Server server;
    private Exceptions exception;

    // svms 웹소켓 관련 설정
    @Getter
    @Setter
    public static class Websocket {

        @NotBlank(message = "svms websocket url 을(를) 찾을 수 없습니다.")
        private String url;

    }

    // svms 서버 관련 설정
    @Getter
    @Setter
    public static class Server {

        @NotBlank(message = "svms server url 을(를) 찾을 수 없습니다.")
        private String url;

    }

    // svms 이벤트 예외 관련 설정
    @Getter
    @Setter
    public static class Exceptions {

        private String[] equals;

        private String[] prefix;

        private String[] contains;

    }

}
