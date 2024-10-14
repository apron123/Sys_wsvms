package com.ziumks.wsvms;
//
//import com.google.gson.JsonSyntaxException;
//import com.ziumks.wsvms.config.api.CommonApiProperties;
//import com.ziumks.wsvms.config.api.SvmsApiProperties;
//import com.ziumks.wsvms.config.api.WidgetEventProperties;
//import com.ziumks.wsvms.model.dto.geoserver.GeoServerCacheDto;
//import com.ziumks.wsvms.model.dto.svms.SvmsCacheDto;
//import com.ziumks.wsvms.model.dto.svms.SvmsSocketMessageDto;
//import com.ziumks.wsvms.service.CacheService;
//import com.ziumks.wsvms.service.EventSendService;
//import com.ziumks.wsvms.exception.HttpConnectionException;
//
//import com.ziumks.wsvms.util.Utils;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockedStatic;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.Arrays;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//@DisplayName("svms 이벤트 발신 테스트")
//class TestEventSendService {
//
//    @InjectMocks
//    private EventSendService eventSendService;
//
//    @Mock
//    private WidgetEventProperties widgetEventProps;
//
//    @Mock
//    private CommonApiProperties commonApiProps;
//
//    @Mock
//    private SvmsApiProperties svmsApiProps;
//
//    @Mock
//    private CacheService cacheService;
//
//    private final MockedStatic<Utils> mUtils = mockStatic(Utils.class);
//
//    @BeforeEach
//    public void init() throws HttpConnectionException {
//
//        // Mocking getSvmsCacheList
//        List<SvmsCacheDto> svmsCacheList = Arrays.asList(
//                new SvmsCacheDto("CameraGUID1", "rstp://connecturl1.com"),
//                new SvmsCacheDto("CameraGUID2", "rstp://connecturl2.com")
//        );
//        lenient().when(cacheService.getSvmsCacheList()).thenReturn(svmsCacheList);
//
//        // Mocking getGeoServerCacheList
//        List<GeoServerCacheDto> geoServerCacheList = Arrays.asList(
//                new GeoServerCacheDto("rstp://connecturl1.com", "CCTV1", 37.5665, 126.9780),
//                new GeoServerCacheDto("rstp://connecturl2.com", "CCTV2", 35.1796, 129.0756)
//        );
//        lenient().when(cacheService.getGeoServerCacheList()).thenReturn(geoServerCacheList);
//
//        // Mocking svms props exception
//        String[] equalsArray = {"NMS", "F-CA1", "121-CA6(FR)"};
//        String[] prefixArray = {"웅천", "군사경찰_동편15_1", "홍성"};
//        String[] containsArray = {"경찰"}; // 빈 배열 초기화
//        SvmsApiProperties.Exceptions exception = new SvmsApiProperties.Exceptions();
//        exception.setEquals(equalsArray);
//        exception.setPrefix(prefixArray);
//        exception.setContains(containsArray);
//
//        lenient().when(svmsApiProps.getException()).thenReturn(exception);
//
//        // Mocking common props widget
//        CommonApiProperties.Widget widget = new CommonApiProperties.Widget();
//        widget.setUrl("morkingWidegetUrl");
//        lenient().when(commonApiProps.getWidget()).thenReturn(widget);
//
//        // Mocking sendWidgetEvent
//        mUtils.when(()->Utils.sendWidgetEvent(anyString(), any())).thenReturn(null);
//
//    }
//
//    @Test
//    @DisplayName("svms 이벤트 발신 테스트")
//    void testSetSvmsEventCctv() throws NullPointerException, JsonSyntaxException {
//
//        // Given
//        SvmsSocketMessageDto svmsSocketMessageDto = SvmsSocketMessageDto.builder()
//                .deviceGUID("CameraGUID2")
//                .deviceName("CCTV2")
//                .build();
//        String message = Utils.gson.toJson(svmsSocketMessageDto);
//
//        // Then
//        assertDoesNotThrow(() -> eventSendService.setSvmsEventCctv(message));
//
//    }
//
//    @Test
//    @DisplayName("2초 이내에 중복 발생한 이벤트 처리 테스트")
//    void testSetSvmsEventCctv_checkEventRecurrence() throws NullPointerException, JsonSyntaxException, InterruptedException {
//
//        // Given
//        SvmsSocketMessageDto svmsSocketMessageDto = SvmsSocketMessageDto.builder()
//                .deviceGUID("CameraGUID2")
//                .deviceName("CCTV2")
//                .build();
//        String message = Utils.gson.toJson(svmsSocketMessageDto);
//
//        // Then
//        assertDoesNotThrow(() -> {
//            eventSendService.setSvmsEventCctv(message);
//            Thread.sleep(1000); // 1초 대기
//            eventSendService.setSvmsEventCctv(message);
//            Thread.sleep(2000); // 2초 대기
//            eventSendService.setSvmsEventCctv(message);
//        });
//
//    }
//
//    @Test
//    @DisplayName("이벤트 이름 패턴에 따른 예외처리 테스트")
//    void testSetSvmsEventCctv_checkEventNamePattern() throws NullPointerException, JsonSyntaxException, InterruptedException {
//
//        // Given
//        SvmsSocketMessageDto svmsSocketMessageDto = SvmsSocketMessageDto.builder()
//                .deviceGUID("CameraGUID2")
//                .deviceName("NMS")
//                .build();
//        String equalsMessage = Utils.gson.toJson(svmsSocketMessageDto);
//
//        SvmsSocketMessageDto svmsSocketMessageDto2 = SvmsSocketMessageDto.builder()
//                .deviceGUID("CameraGUID2")
//                .deviceName("웅천 NMS")
//                .build();
//        String prefixMessage = Utils.gson.toJson(svmsSocketMessageDto2);
//
//        // Then
//        assertDoesNotThrow(() -> {
//            eventSendService.setSvmsEventCctv(equalsMessage);
//            Thread.sleep(3000); // 3초 대기
//            // Act
//            eventSendService.setSvmsEventCctv(prefixMessage);
//        });
//
//    }
//
//}