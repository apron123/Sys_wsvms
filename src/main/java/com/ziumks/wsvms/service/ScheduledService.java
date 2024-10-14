package com.ziumks.wsvms.service;

import com.ziumks.wsvms.exception.HttpConnectionException;
import com.ziumks.wsvms.websocket.WebSocketManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * 스케쥴러 서비스 로직 - Start Point
 *
 * @author  이상민
 * @since   2024.05.21 16:30
 */
@Slf4j
@RequiredArgsConstructor
@Service("scheduledService")
public class ScheduledService {

    private final CacheService cacheService;

    private final WebSocketManager webSocketManager;

    /**
     *  웹소켓 재연결시도 스케쥴러
     */
    @Scheduled(cron = "0 */5 * * * *")
    public void reconnectWebSocket() {
        log.info("reconnect WebSocket start ..");
        webSocketManager.reconnect();
    }

    /**
     *  svms cctv 리스트 캐시값 갱신
     */
    @Scheduled(cron = "0 0 4 * * *")
    public void getSvmsCacheList() throws HttpConnectionException {
        log.info("Scheduled Task getSvmsCacheList re-start ..");
        cacheService.deleteCacheEntry("'svms'");
        cacheService.getSvmsCacheList();
    }

    /**
     *  geoserver data 리스트 캐시값 갱신
     */
    @Scheduled(cron = "0 50 3 * * *")
    public void getGeoServerCacheList() throws HttpConnectionException {
        log.info("Scheduled Task getSvmsCacheList re-start ..");
        cacheService.deleteCacheEntry("'geoserver'");
        cacheService.getGeoServerCacheList();
    }

}
