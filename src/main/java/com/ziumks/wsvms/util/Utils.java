package com.ziumks.wsvms.util;

import com.google.gson.Gson;
import com.ziumks.wsvms.config.api.WidgetEventProperties;
import com.ziumks.wsvms.exception.HttpConnectionException;
import com.ziumks.wsvms.model.dto.common.BulkRequestDto;
import com.ziumks.wsvms.model.dto.common.BulkResponseDto;
import com.ziumks.wsvms.model.dto.common.SysMonitoringDto;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 공통 메소드 묶어놓은 클래스
 *
 * @author  김주현
 * @since   2024.05.21 16:30
 */
@Slf4j
@UtilityClass
public class Utils {

    public static final Gson gson = new Gson();

    public static final HttpConnection<String> httpConnection = new HttpConnection<>();

    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
    
//    /***  @Table 어노테이션으로 테이블 명 가져오는 메서드
//     * @param entityClass Class<?>
//     * @return string
//     */
//    public static String getTableName(Class<?> entityClass) {
//        Table tableAnnotation = entityClass.getAnnotation(Table.class);
//        if (tableAnnotation != null) {
//            return tableAnnotation.name();
//        }
//        return null; // 테이블명이 지정되지 않은 경우
//    }
//
//    /**
//     * Id 어노테이션으로 id값 가져오는 메서드
//     *
//     * @param entityClass Class<?>
//     * @return string
//     */
//    public static String getIdName(Class<?> entityClass) {
//        for (Field field : entityClass.getDeclaredFields()) {
//            if (field.isAnnotationPresent(Id.class)) {
//                return field.getName();
//            }
//        }
//        return null; // @Id 애노테이션이 없는 경우
//    }

    /*** 정규표현식 사용하여 숫자 말고 전부 제거
     * @param input string
     * @return string
     */
    public static String regexMethod(String input) {

        // 방법 1: 정규표현식 사용
        String result = input.replaceAll("[^0-9.]+", "");

        return result;

    }

    /*** Xml data를 변환 class 객체로 parser
     * @param xmlData string
     * @param targetClass Class<T>
     * @return Class<T>
     */
    public static <T> T xmlToObject(String xmlData, Class<T> targetClass) {

        try {

            // JAXBContext 객체 생성
            JAXBContext jaxbContext = JAXBContext.newInstance(targetClass);
            // Unmarshaller 생성
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

            StringReader reader = new StringReader(xmlData);

            // Unmarshal 수행하여 XML을 객체로 변환
            return targetClass.cast(unmarshaller.unmarshal(reader));

        } catch (JAXBException e) {

            e.printStackTrace();
            // 여기서 언마샬 오류 처리를 수행할 수 있습니다.

            return null;

        }
    }

    /**
     * Bulk 서버와 http 통신 메서드
     *
     * @param  bulkUrl  Bulk 서버 url
     * @param  bulkRequestDto Bulk 서버 request json data
     * @return BulkResponseDto
     */
    public static BulkResponseDto sendInsertBulk(String bulkUrl, BulkRequestDto bulkRequestDto) throws HttpConnectionException {

        log.info("Send Insert Bulk Start");
        Map<String, Object> reqParam = new HashMap<>();
        reqParam.put("dataList", bulkRequestDto.getDataList());
        reqParam.put("indexName", bulkRequestDto.getIndexName());
        reqParam.put("docId", bulkRequestDto.getDocId());
        reqParam.put("tableName", bulkRequestDto.getTableName());
        String jsonString = gson.toJson(reqParam);

        Map<String, Object> reqHeader = new HashMap<>();
        reqHeader.put(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        BulkResponseDto response = gson.fromJson(httpConnection.doPost(bulkUrl + "/insert", reqHeader, jsonString), BulkResponseDto.class);
        log.info(response.getMsg());

        return response;

    }

    /**
     * System 모니터링 서버와 http 통신 메서드
     *
     * @param  sysUrl  Sys 서버 url
     * @param  sysMonitoringStatusDto Sys 서버 status flag json data
     * @return ResponseEntity
     */
    public static ResponseEntity sendUpdateSys(String sysUrl, SysMonitoringDto sysMonitoringStatusDto) throws HttpConnectionException {

        log.info("Send Update Sys Start");

        Map<String, Object> reqParam = new HashMap<>();
        reqParam.put("schemaName", sysMonitoringStatusDto.getSchemaName());
        reqParam.put("tableName", sysMonitoringStatusDto.getTableName());
        reqParam.put("crawlerStatus", sysMonitoringStatusDto.getCrawlerStatus());
        reqParam.put("saveStatus", sysMonitoringStatusDto.getSaveStatus());
        reqParam.put("elasticStatus", sysMonitoringStatusDto.getElasticStatus());
        String jsonString = gson.toJson(reqParam);

        Map<String, Object> reqHeader = new HashMap<>();
        reqHeader.put(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        ResponseEntity response = gson.fromJson(httpConnection.doPut(sysUrl + "/update", reqHeader, jsonString), ResponseEntity.class);
        log.info(response.getStatusCode().getReasonPhrase());

        return response;

    }

    /**
     * widget 서버와 http 통신 메서드
     *
     * @param  widgetUrl  widget 서버 url
     * @param  rinoEventRegister widget 서버 request event json data
     * @return String 메세지
     */
    public static String sendWidgetEvent(String widgetUrl, WidgetEventProperties rinoEventRegister) throws HttpConnectionException {

        log.info("Send Widget Event Start");

        // 현재 시간 가져오기
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

        // 현재 시간을 문자열로 변환
        String formattedTime = currentTime.format(formatter);
        rinoEventRegister.setOutbDtm(formattedTime);
        // 이벤트 발송
        Map<String, Object> reqParam = rinoEventRegister.getMap();
        Map<String, Object> reqHeader = new HashMap<>();
        reqHeader.put(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);

        log.info("--- event request ---");
        log.info(rinoEventRegister.toString());
        String response = httpConnection.doPost(widgetUrl, reqHeader, reqParam);
        log.info(response);

        return response;

    }

    // 현재 시간 return 하는 함수
    public static Timestamp getCurrTimestamp() { return new Timestamp(new Date().getTime()); }

    /**
     * 1. entity to map
     * 2. 만약 값이 timestamp type 이면 -변환함.
     **/
    public static <T> Map<String, Object> mapEntityToMap(T entity) {

        Map<String, Object> map = new HashMap<>();

        // WeatherHtmlCrawlerEntity의 모든 필드를 가져와서 매핑
        Field[] fields = entity.getClass().getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true); // private 필드에 접근하기 위해

            try {
                Object value = field.get(entity);

                // Timestamp 타입인 경우 Date로 변환
                if (value instanceof Timestamp) {
                    value = ((Timestamp) value).toLocalDateTime();
                }
                //map에 담기
                map.put(field.getName(), value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return map;

    }

    public static Timestamp strToTimeStamp(String strTimeData) {

        String pattern = "yyyyMMddHHmmss";
        if(strTimeData == null){
            return null;
        }
        String []splitTime = strTimeData.split("\\.");

        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);

        Timestamp timestamp = null;
        try {
            timestamp = new Timestamp(dateFormat.parse(splitTime[0]).getTime());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return timestamp;

    }

    /**
     * 문자열을 LocalDateTime으로 변환하는 메서드
     *
     * @param dateTimeString 날짜 시간 문자열
     * @return LocalDateTime 객체
     * @throws DateTimeParseException 형식이 잘못된 경우 예외 발생
     */
    public static LocalDateTime strToLocalDateTime(String dateTimeString) throws DateTimeParseException {
        if (dateTimeString == null || dateTimeString.isEmpty()) {
            return null;
        }
        return LocalDateTime.parse(dateTimeString, formatter);
    }

}
