package com.ziumks.wsvms.config.api;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * widget 이벤트 request
 *
 * @author  이상민
 * @since   2024.05.21 16:30
 */
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties("widget.api")
@Configuration
public class WidgetEventProperties {

    //클라이언트 코드
    private String clientCd;
    
    //사이트 코드
    private String siteCd;

    //이벤트 그룹 코드
    private String evetGbCd;

    //통계용
    private String outbMainGb;
    
    //이벤트 발생 시간 - 이것만 현재 시간으로 바꿔주면 됨
    private String outbDtm;

    //이벤트 등급
    private String statEvetCd;
    
    //이벤트 내용
    private String statEvetCntn;

    //발생 이벤트 이름 "저시정 발생"
    private String statEvetNm;

    //이벤트 등급
    private String statEvetGdCd;

    //이벤트 테마 코드
    private String svcThemeCd;

    //이벤트 유닛 서비스 코드
    private String unitSvcCd;

    //이벤트 위도 - x
    private String xCoordinate;

    //이벤트 경도 - y
    private String yCoordinate;

    //이벤트 고도 - z
    private String zCoordinate;

    //위치 코드
    private String znCd;

    //이벤트 장소
    private String outbPlac;

    /**
     * 맵으로 파싱 메서드
     *
     * @return  Map
     */
    public Map<String, Object> getMap() {

        Map<String, Object> map = new HashMap<>();
        map.put("clientCd", this.getClientCd());
        map.put("siteCd", this.getSiteCd());
        map.put("evetGbCd", this.getEvetGbCd());
        map.put("outbMainGb", this.getOutbMainGb());
        map.put("outbDtm", this.getOutbDtm());
        map.put("statEvetCd", this.getStatEvetCd());
        map.put("statEvetCntn", this.getStatEvetCntn());
        map.put("statEvetNm", this.getStatEvetNm());
        map.put("statEvetGdCd", this.getStatEvetGdCd());
        map.put("svcThemeCd", this.getSvcThemeCd());
        map.put("unitSvcCd", this.getUnitSvcCd());
        map.put("xCoordinate", this.getXCoordinate());
        map.put("yCoordinate", this.getYCoordinate());
        map.put("zCoordinate", this.getZCoordinate());
        map.put("znCd", this.getZnCd());
        map.put("outbPlac", this.getOutbPlac());

        return map;
    }

}
