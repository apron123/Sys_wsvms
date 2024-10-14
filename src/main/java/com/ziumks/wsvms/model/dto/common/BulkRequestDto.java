package com.ziumks.wsvms.model.dto.common;


import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.List;
import java.util.Map;

/**
 * Bulk 서버 request dto
 *
 * @author  김주현
 * @since   2024.05.21 16:30
 */
@ToString
@Builder
@Data
public class BulkRequestDto {
    
    private String tableName;
    private String indexName;
    private String docId;
    private List<Map<String,Object>> dataList;

}
