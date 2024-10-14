package com.ziumks.wsvms.util.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SysStatus {

    CRAWLER("crawler"),
    SAVE("save"),
    ELASTIC("elastic");

    private final String value;

}
