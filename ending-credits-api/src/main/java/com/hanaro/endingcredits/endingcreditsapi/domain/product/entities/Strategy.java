package com.hanaro.endingcredits.endingcreditsapi.domain.product.entities;

public enum Strategy {
    AGGRESSIVE("공격적"),
    STABLE("안정적"),
    SHORT_TERM("단기"),
    LONG_TERM("장기"),
    LOW_COST("저비용"),
    STABLE_PROFIT("수익 안정성"),
    RISK_TOLERANT("위험 감수형");

    private final String description;

    Strategy(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static Strategy fromDescription(String description) {
        for (Strategy strategy : values()) {
            if (strategy.getDescription().equals(description)) {
                return strategy;
            }
        }
        throw new IllegalArgumentException("Invalid strategy description: " + description);
    }
}
