package com.ziumks.wsvms.model.dto.geoserver;

import lombok.*;

/**
 * geoserver cache 데이터 dto
 *
 * @author  이상민
 * @since   2024.05.21 16:30
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GeoServerCacheDto {

    private String deviceId;

    private String cctvNm;

    private double lat;

    private double lon;

}
