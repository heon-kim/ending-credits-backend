package com.hanaro.endingcredits.endingcreditsapi.domain.product.entities;

import java.util.HashMap;
import java.util.Map;

public enum SysType {
    DB(1),
    DC(2),
    IRP(3);

    private static final Map<Integer, SysType> codeToTypeMap = new HashMap<>();

    static {
        for (SysType type : SysType.values()) {
            codeToTypeMap.put(type.code, type);
        }
    }

    private final int code;

    SysType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static SysType fromCode(int code) {
        return codeToTypeMap.getOrDefault(code, null);
    }
}
