package com.ziumks.wsvms.service;

import com.ziumks.wsvms.config.api.GeoserverApiProperties;
import com.ziumks.wsvms.config.api.SvmsApiProperties;
import com.ziumks.wsvms.model.dto.geoserver.GeoServerCacheDto;
import com.ziumks.wsvms.model.dto.geoserver.GeoServerDto;
import com.ziumks.wsvms.model.mapper.SerDeMapper;
import com.ziumks.wsvms.model.dto.svms.SvmsCacheDto;
import com.ziumks.wsvms.model.dto.svms.SvmsCctvDto;
import com.ziumks.wsvms.exception.HttpConnectionException;
import com.ziumks.wsvms.util.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 데이터를 캐시에 저장하는 서비스 로직
 *
 * @author  이상민
 * @since   2024.05.21 16:30
 */
@Slf4j
@RequiredArgsConstructor
@CacheConfig(cacheNames = "localCacheStore")
@Service("cacheService")
public class CacheService {

    private final SvmsApiProperties svmsApiProps;

    private final GeoserverApiProperties geoserverApiProps;

    private final SerDeMapper serDeMapper;

    /**
     * svms CCTV 리스트 캐시에 저장
     *
     * @return svms 서버에서 받아온 CCTV 리스트를 캐시에 저장하고 반환
     * @throws HttpConnectionException HTTP 연결 중 발생하는 예외
     * @throws NullPointerException Null 포인터 예외
     */
    @Cacheable(key = "'svms'")
    public List<SvmsCacheDto> getSvmsCacheList() throws HttpConnectionException, NullPointerException {
        // svms 서버에 데이터 요청
        log.info("get SvmsCacheList start");
        // 캐시로 저장할 dto로 파싱
        return serDeMapper.fromJsonToList(Utils.httpConnection.doGet(svmsApiProps.getServer().getUrl(), null, null), SvmsCctvDto.class).stream()
                .map(svmsCctv -> SvmsCacheDto.builder()
                        .cameraGUID(svmsCctv.getCameraGUID())
                        .connectURL(svmsCctv.getConnectURL())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * geoserver 데이터 리스트 캐시에 저장
     *
     * @return geoserver 서버에서 받아온 features 리스트를 캐시에 저장하고 반환
     * @throws HttpConnectionException HTTP 연결 중 발생하는 예외
     * @throws NullPointerException Null 포인터 예외
     */
    @Cacheable(key = "'geoserver'")
    public List<GeoServerCacheDto> getGeoServerCacheList() throws HttpConnectionException, NullPointerException {
        // geoserver에 데이터 요청
        log.info("get GeoServerCacheList start");
        // 캐시로 저장할 dto로 파싱
        return serDeMapper.fromJson(Utils.httpConnection.doGet(geoserverApiProps.getUrl(), null, null), GeoServerDto.class)
                .getFeatures().stream()
                .map(features -> GeoServerCacheDto.builder()
                        .deviceId(features.getProperties().getDeviceId())
                        .cctvNm(features.getProperties().getCctvNm())
                        .lat(features.getGeometry().getCoordinates().get(0))
                        .lon(features.getGeometry().getCoordinates().get(1))
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * 캐시 데이터 삭제
     *
     * @param  cacheKey  키에 매핑되는 캐시 데이터 삭제
     */
    @CacheEvict(value = "#root.target.cacheStore", key = "#cacheKey")
    public void deleteCacheEntry(String cacheKey) {
        log.info("Delete Cache List for key: " + cacheKey);
    }

}
