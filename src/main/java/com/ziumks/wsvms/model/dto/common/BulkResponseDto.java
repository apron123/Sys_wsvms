package com.ziumks.wsvms.model.dto.common;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/**
 * Bulk 서버 response dto
 *
 * @author  김주현
 * @since   2024.05.21 16:30
 */
@ToString
@Builder
@Data
public class BulkResponseDto {

    private int responseCode;
    private String tableName;
    private String indexName;
    private String msg;

}
