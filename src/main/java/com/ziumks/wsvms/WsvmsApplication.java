package com.ziumks.wsvms;

import com.ziumks.wsvms.service.CacheService;
import com.ziumks.wsvms.exception.HttpConnectionException;
import com.ziumks.wsvms.websocket.WebSocketManager;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@RequiredArgsConstructor
@SpringBootApplication
public class WsvmsApplication implements CommandLineRunner {

    private final WebSocketManager webSocketManager;

    private final CacheService cacheService;

    public static void main(String[] args) {SpringApplication.run(WsvmsApplication.class, args);}

    @Override
    public void run(String... args) throws HttpConnectionException {
        // CCTV 리스트 데이터 캐시에 저장
        cacheService.getSvmsCacheList();
        cacheService.getGeoServerCacheList();
        // 애플리케이션이 시작될 때 WebSocket 연결 시작
        webSocketManager.connect();
    }

}
