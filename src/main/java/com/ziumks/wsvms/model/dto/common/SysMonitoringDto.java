package com.ziumks.wsvms.model.dto.common;

import com.ziumks.wsvms.util.enums.Status;
import com.ziumks.wsvms.util.enums.SysStatus;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * 체계 수집 상태 체크 Flag DTO
 *
 * @author  이상민
 * @since   2024.07.08 12:00
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SysMonitoringDto {

    private String schemaName;

    private String tableName;

    @Builder.Default
    private int crawlerStatus = 0;

    @Builder.Default
    private int saveStatus = 0;

    @Builder.Default
    private int elasticStatus = 0;

    private String msg;

    @Builder.Default
    private String collectorStatus = Status.UP.getValue();

    @Builder.Default
    private String dataStatus = Status.DOWN.getValue();

    @Builder.Default
    private LocalDateTime collectorTime = LocalDateTime.now();

    @Builder.Default
    private LocalDateTime dataTime = LocalDateTime.now();

    public void updateStatus(SysStatus status) {
        switch (status) {
            case CRAWLER:
                this.crawlerStatus = 1;
                break;

            case SAVE:
                this.saveStatus = 1;
                break;

            case ELASTIC:
                this.elasticStatus = 1;
                break;

            default:
                throw new IllegalArgumentException("Unexpected status(CRAWLER|SAVE|ELASTIC) : " + status);
        }
    }

    public SysMonitoringDto updateDataStatus() {
        if (this.saveStatus == 1) {
            this.dataStatus = Status.UP.getValue();
        }
        return this;
    }

}
