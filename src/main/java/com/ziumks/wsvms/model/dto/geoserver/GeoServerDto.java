package com.ziumks.wsvms.model.dto.geoserver;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

/**
 * geoserver json 데이터 dto
 *
 * @author  이상민
 * @since   2024.05.21 16:30
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GeoServerDto {

    private String type;

    private List<FeaturesDto> features;

    private String serverKey;

    private String cameraGUID;

    private String cameraIPAddress;

    private String serviceAddress;

    private String cameraLocation;

    private String cameraManufactureCompany;

    private String cameraFirewareVersion;

    private String cameraModelName;

    private String cameraMAC;

    private String cameraSerialNumber;

    private String cameraName;

    private int cameraRTSPPort;

    private String cameraRTSPURL;

    private String connectURL;

    private int findLiveStreamTryCount;

    private boolean isActive;

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class FeaturesDto {

        private String type;

        private String id;

        private GeometryDto geometry;

        @JsonProperty("geometry_name")
        private String geometryName;

        private PropertiesDto properties;

        @Getter
        @Builder
        @AllArgsConstructor
        @NoArgsConstructor(access = AccessLevel.PROTECTED)
        public static class GeometryDto {

            private String type;

            private List<Double> coordinates;

        }

        @Getter
        @Builder
        @AllArgsConstructor
        @NoArgsConstructor(access = AccessLevel.PROTECTED)
        public static class PropertiesDto {

            @JsonProperty("mgr_no")
            private String mgrNo;

            @JsonProperty("org_mgr_no")
            private String orgMgrNo;

            @JsonProperty("md_mgr_no")
            private String mdMgrNo;

            @JsonProperty("vms_mgr_no")
            private String vmsMgrNo;

            @JsonProperty("site_mgr_no")
            private String siteMgrNo;

            @JsonProperty("cctv_nm")
            private String cctvNm;

            @JsonProperty("inst_dat")
            private String instDat;

            @JsonProperty("device_id")
            private String deviceId;

            @JsonProperty("chnl_no")
            private int chnlNo;

            @JsonProperty("gbn_cd")
            private String gbnCd;

            @JsonProperty("ip_addr")
            private String ipAddr;

            @JsonProperty("port_num")
            private int portNum;

        }


    }

}
