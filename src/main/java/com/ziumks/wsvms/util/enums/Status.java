package com.ziumks.wsvms.util.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Status {

    UP("up"),
    DOWN("down");

    private final String value;

    public static void checkStatus(String status) {
        for (Status value : Status.values()) {
            if (value.getValue().equals(status)) {
                return;
            }
        }
        throw new IllegalArgumentException("유효하지 않은 문자(up|down) : " + status);
    }

}
