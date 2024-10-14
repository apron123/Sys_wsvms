package com.ziumks.wsvms.service;


import com.google.gson.JsonSyntaxException;
import com.ziumks.wsvms.config.api.CommonApiProperties;
import com.ziumks.wsvms.config.api.SvmsApiProperties;
import com.ziumks.wsvms.config.api.WidgetEventProperties;
import com.ziumks.wsvms.model.dto.geoserver.GeoServerCacheDto;
import com.ziumks.wsvms.model.dto.svms.SvmsSocketMessageDto;
import com.ziumks.wsvms.model.dto.svms.SvmsCacheDto;
import com.ziumks.wsvms.exception.HttpConnectionException;
import com.ziumks.wsvms.model.mapper.SerDeMapper;
import com.ziumks.wsvms.util.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

/**
 *  svms, geoserver간의 CCTV 데이터를 매핑해서 이벤트 발생 서비스 로직
 *
 * @author  이상민
 * @since   2024.05.21 16:30
 */
@Slf4j
@RequiredArgsConstructor
@Service("eventSendService")
public class EventSendService {

    private final CacheService cacheService;

    private final WidgetEventProperties widgetEvent;

    private final SvmsApiProperties svmsApiProps;

    private final CommonApiProperties commonApiProps;

    private final SerDeMapper serDeMapper;

    // 전 이벤트 발생한 GUID
    private static String prevDeviceGUID;
    // 전 이벤트 발생 시간
    private static Instant prevCurTime = Instant.now();
    // 중복 이벤트 발생 확인 플레그
    private static boolean firstStart = true;

    /**
     * 이벤트 발생한 CCTV 정보 설정하여 이벤트 위젯서버로 발송
     *
     * @param  message  svms 이벤트 발생 CCTV정보
     * @throws HttpConnectionException HTTP 연결 중 발생하는 예외
     * @throws NullPointerException Null 포인터 예외
     * @throws JsonSyntaxException  메시지의 JSON 형식이 잘못된 경우 발생하는 예외
     */
    public void setSvmsEventCctv(String message) throws HttpConnectionException, NullPointerException, JsonSyntaxException {

        // svms 이벤트 발생 CCTV정보
        SvmsSocketMessageDto svmsSocketMessage = serDeMapper.fromJson(message, SvmsSocketMessageDto.class);
        log.info("svmsSocketMessage : " + svmsSocketMessage.toString());

        // 2초이내 같은 GUID의 CCTV가 들어올 경우 예외처리
        if (checkEventRecurrence(svmsSocketMessage.getDeviceGUID())) {
            return;
        }

        // CCTV 디바이스 이름 관련 예외처리
        if (checkEventNamePattern(svmsSocketMessage.getDeviceName())) {
            return;
        }

        // 1. event 발생 deviceGUID와 cameras svmsApi CameraGUID와 비교 함
        Optional<String> svmsMappingConnectUrl = cacheService.getSvmsCacheList().stream()
                .filter(svmsCache -> svmsSocketMessage.getDeviceGUID().equals(svmsCache.getCameraGUID()))
                .map(SvmsCacheDto::getConnectURL)
                .findFirst();

        // 2. svmsApiConnectURL 와 Geoserver의 deviceId와 비교하여 lat,lon 가져옴..
        cacheService.getGeoServerCacheList().stream()
                .filter(geoServerCacheDto -> svmsMappingConnectUrl.isPresent() && geoServerCacheDto.getDeviceId().contains(svmsMappingConnectUrl.get()))
                .findFirst()
                .ifPresent(geoServerCacheDto -> {
                    log.info("event cctv url : " + svmsMappingConnectUrl.get());
                    widgetEvent.setStatEvetCntn("CCTV : " + geoServerCacheDto.getCctvNm());
                    widgetEvent.setXCoordinate(String.valueOf(geoServerCacheDto.getLat()));
                    widgetEvent.setYCoordinate(String.valueOf(geoServerCacheDto.getLon()));
                    // 이벤트 발송
                    try {
                        log.info("widget server event send success");
                        Utils.sendWidgetEvent(commonApiProps.getWidget().getUrl(), widgetEvent);
                    } catch (HttpConnectionException e) {
                        log.info("widget server event send error");
                        log.error(e.getMessage());
                    }
                });
    }

    /**
     * 이벤트 메세지를 dto로 파싱 메서드
     *
     * @param  message  svms 웹소켓 메세지
     * @return  SvmsSocketMessageDto
     */
    public SvmsSocketMessageDto parseMessage(String message) {

        log.info("Received message: " + message);

        try {
            return serDeMapper.fromJson(message, SvmsSocketMessageDto.class);
        } catch (JsonSyntaxException e) {

            log.error(e.getMessage());
            return null; // 변환이 실패한 경우 null 반환

        }

    }

    /**
     * 이벤트가 2초 이내에 재발생 했는지 체크 매서드
     *
     * @param  deviceGUID  svms 이벤트 발생 cctv guid
     * @return  boolean
     */
    public boolean checkEventRecurrence(String deviceGUID) {

        Duration duration = Duration.between(prevCurTime, Instant.now());
        long flagMs = 2000;
        long ms = duration.toMillis();

        log.info(deviceGUID + " " + ms + "ms");
        if (!firstStart && prevDeviceGUID.equals(deviceGUID) && ms < flagMs) {
            log.info("Events that re-occur within 2 seconds");
            return true;
        }

        firstStart = false;
        prevDeviceGUID = deviceGUID;
        prevCurTime = Instant.now();

        return false;

    }

    /**
     * 이벤트가 발생한 cctv 이름이 예외 패턴인지 체크 메서드
     *
     * @param  deviceName  svms 이벤트 발생 cctv name
     * @return  boolean
     */
    public boolean checkEventNamePattern(String deviceName) {

        // equalsArray에 속하는 경우
        if (svmsApiProps.getException().getEquals() != null && Arrays.asList(svmsApiProps.getException().getEquals()).contains(deviceName)) {
            log.info("equalsArray : " + Arrays.toString(svmsApiProps.getException().getEquals()));
            log.info("Device name '{}' matches an entry in equalsArray", deviceName);
            return true;
        }
        // prefixArray에 속하는 경우
        if (svmsApiProps.getException().getPrefix() != null && Arrays.stream(svmsApiProps.getException().getPrefix()).anyMatch(deviceName::startsWith)) {
            log.info("prefixArray" + Arrays.toString(svmsApiProps.getException().getPrefix()));
            log.info("Device name '{}' matches a prefix in prefixArray", deviceName);
            return true;
        }
        // containsArray에 속하는 경우
        if (svmsApiProps.getException().getContains() != null && Arrays.stream(svmsApiProps.getException().getContains()).anyMatch(deviceName::contains)) {
            log.info("containsArray" + Arrays.toString(svmsApiProps.getException().getContains()));
            log.info("Device name '{}' matches a substring in containsArray", deviceName);
            return true;
        }

        return false;

    }

}
