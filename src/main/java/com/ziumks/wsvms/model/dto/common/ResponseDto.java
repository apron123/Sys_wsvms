package com.ziumks.wsvms.model.dto.common;

import lombok.*;
import org.springframework.http.HttpStatus;

/**
 * 공통 response DTO
 *
 * @author  김주현
 * @since   2024.05.21 16:30
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResponseDto<T> {

    private int resultCode;

    private T resultData;

    private String resultMsg;

    private boolean resultFlag;

    public ResponseDto(T resultData) {
        this.resultCode = HttpStatus.OK.value();
        this.resultData = resultData;
        this.resultFlag = true;
    }

    public ResponseDto(HttpStatus httpStatus) {
        this.resultCode = httpStatus.value();
        this.resultMsg = httpStatus.getReasonPhrase();
        this.resultFlag = httpStatus.value() == 200;
    }

}
