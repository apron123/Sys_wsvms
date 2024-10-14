package com.ziumks.wsvms.model.dto.svms;

import lombok.*;

/**
 * svms json 데이터 dto
 *
 * @author  이상민
 * @since   2024.05.21 16:30
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SvmsCctvDto {

    private int userState;

    private String userMsg;

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

}
