package com.ziumks.wsvms.websocket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

/**
 *  websocket 세션 연결 매니저
 *
 * @author  이상민
 * @since   2024.05.21 16:30
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class WebSocketManager {

    private final ClientWebSocketHandler clientWebSocketHandler;

    private WebSocketConnectionManager webSocketConnectionManager;

    @Value("${svms.websocket.url}")
    private String svmsWsUrl;

    /**
     *  웹소켓 클라이언트 객체 생성 후 세션 연결 메서드
     */
    public void connect() {
        WebSocketClient webSocketClient = new StandardWebSocketClient();
        webSocketConnectionManager = new WebSocketConnectionManager(webSocketClient, clientWebSocketHandler, svmsWsUrl);
        webSocketConnectionManager.setAutoStartup(true);
        webSocketConnectionManager.start();
    }

    /**
     *  웹소켓 연결이 끊겼을 경우 재연결 시도 메서드
     */
    public void reconnect() {
        if(webSocketConnectionManager.isConnected()) {
            log.info("webSocket already connected");
        } else {
            webSocketConnectionManager.start();
        }
    }

}
