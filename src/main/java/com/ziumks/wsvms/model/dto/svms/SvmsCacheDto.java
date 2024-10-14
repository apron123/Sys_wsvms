package com.ziumks.wsvms.model.dto.svms;

import lombok.*;

/**
 * svms cache 데이터 dto
 *
 * @author  이상민
 * @since   2024.05.21 16:30
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SvmsCacheDto {

    private String cameraGUID;

    private String connectURL;

}
