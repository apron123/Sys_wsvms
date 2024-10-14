package com.ziumks.wsvms.websocket;

import com.ziumks.wsvms.service.EventSendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

/**
 * WebSocket 클라이언트 메시지 처리 핸들러
 * 이 클래스는 웹소켓 연결에 대한 이벤트를 처리합니다.
 * - 연결이 성립되었을 때
 * - 메시지가 수신되었을 때
 * - 연결 오류가 발생했을 때
 * - 연결이 종료되었을 때
 *
 * @author  이상민
 * @since   2024.05.21 16:30
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class ClientWebSocketHandler implements WebSocketHandler {

    private final EventSendService eventSendService;

    /**
     * 웹소켓 연결이 성공 되었을 때 호출됩니다.
     *
     * @param  session  웹소켓 세션
     * @throws Exception  연결 작업 중 예외가 발생할 경우
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("Connected to server");
        // 연결이 성공하면 필요한 작업을 수행할 수 있습니다.
    }

    /**
     * 웹소켓으로부터 메시지를 수신했을 때 호출됩니다.
     *
     * @param  session  웹소켓 세션
     * @param  message  수신된 웹소켓 메시지
     * @throws Exception  메시지 처리 중 예외가 발생할 경우
     */
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        log.info("Received message: {}", message.getPayload());
        // 수신한 메시지를 처리할 수 있습니다.
        eventSendService.setSvmsEventCctv(message.getPayload().toString());
    }

    /**
     * 웹소켓 통신 중 오류가 발생했을 때 호출됩니다.
     *
     * @param  session  웹소켓 세션
     * @param  exception  발생한 예외
     * @throws Exception  오류 처리 중 예외가 발생할 경우
     */
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.error("WebSocket connection error: {}", exception.getMessage());
        // 연결 오류가 발생했을 때 처리할 작업을 수행할 수 있습니다.
    }

    /**
     * 웹소켓 연결이 종료되었을 때 호출됩니다.
     *
     * @param  session  웹소켓 세션
     * @param  closeStatus  연결 종료 상태
     * @throws Exception  연결 종료 처리 중 예외가 발생할 경우
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        log.info("Connection closed: {}", closeStatus.getReason());
    }

    /**
     * 부분 메시지의 처리를 지원하는지 여부를 반환합니다.
     *
     * @return false - 부분 메시지 처리를 지원하지 않습니다.
     */
    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

}
