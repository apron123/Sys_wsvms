package com.ziumks.wsvms.model.dto.svms;

import lombok.*;

/**
 * svms 이벤트 발생 cctv 데이터 dto
 *
 * @author  이상민
 * @since   2024.05.21 16:30
 */
@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SvmsSocketMessageDto {

    private String deviceGUID;

    private String deviceName;

}
