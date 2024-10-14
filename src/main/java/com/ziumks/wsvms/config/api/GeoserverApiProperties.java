package com.ziumks.wsvms.config.api;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * geoserver api 시스템 프로퍼티 값 주입
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
@ConfigurationProperties(prefix = "geoserver.server")
@Configuration
public class GeoserverApiProperties {

    @NotBlank(message = "geoserver url 을(를) 찾을 수 없습니다.")
    private String url;

}
