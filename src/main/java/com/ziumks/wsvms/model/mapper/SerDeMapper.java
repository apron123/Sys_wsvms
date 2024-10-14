package com.ziumks.wsvms.model.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Json & Xml & Map 등 매핑을 처리하는 객체
 *
 * @author  이상민
 * @since   2024.07.03 16:30
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class SerDeMapper {

    private final ObjectMapper objectMapper;

    /**
     * 객체를 JSON 문자열로 변환하는 메서드
     * @param object 변환할 객체
     * @return String 변환된 JSON 문자열
     */
    public String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting object to JSON", e);
        }
    }

    /**
     * JSON 문자열을 객체로 변환하는 메서드
     * @param json 변환할 JSON 문자열
     * @param clazz 변환할 객체의 클래스 타입
     * @param <T> 객체의 타입
     * @return T 변환된 객체
     */
    public <T> T fromJson(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting JSON to object", e);
        }
    }

    /**
     * JSON 문자열을 List로 변환하는 메서드
     * @param json 변환할 JSON 문자열
     * @param clazz 변환할 객체의 클래스 타입
     * @param <T> 객체의 타입
     * @return List<T> 변환된 객체 리스트
     */
    public <T> List<T> fromJsonToList(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, new TypeReference<List<T>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting JSON to List", e);
        }
    }

    /**
     * DTO를 Map<String, Object>으로 변환하는 메서드
     * @param dto 변환할 DTO 객체
     * @return Map<String, Object> 변환된 Map 데이터
     */
    public <T> Map<String, Object> toMap(T dto) {
        try {
            return objectMapper.convertValue(dto, new TypeReference<Map<String, Object>>() {});
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Error converting DTO to Map", e);
        }
    }


}
