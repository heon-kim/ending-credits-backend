package com.hanaro.endingcredits.endingcreditsapi.domain.product.entities;

import java.util.HashMap;
import java.util.Map;

public enum ProductArea {
    BANK("은행", 1),
    STOCK("증권", 2),
    ASSET_MANAGEMENT("자산운용", 3),
    LIFE_INSURANCE("생명보험", 4),
    GENERAL_INSURANCE("손해보험", 5);

    private static final Map<String, ProductArea> descriptionToAreaMap = new HashMap<>();
    private static final Map<Integer, ProductArea> codeToAreaMap = new HashMap<>();

    static {
        for (ProductArea area : ProductArea.values()) {
            descriptionToAreaMap.put(area.description, area);
            codeToAreaMap.put(area.code, area);
        }
    }

    private final String description;
    private final int code;

    ProductArea(String description, int code) {
        this.description = description;
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public int getCode() {
        return code;
    }

    public static ProductArea fromCode(int code) {
        return codeToAreaMap.getOrDefault(code, null);
    }

    public static ProductArea fromDescription(String description) {
        return descriptionToAreaMap.getOrDefault(description, null);
    }
}