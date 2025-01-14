package com.hanaro.endingcredits.endingcreditsapi.domain.product.entities;

public enum ProductArea {
    BANK("은행"),
    ASSET_MANAGEMENT("자산운용"),
    LIFE_INSURANCE("생명보험"),
    GENERAL_INSURANCE("손해보험");

    private final String description;

    ProductArea(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}