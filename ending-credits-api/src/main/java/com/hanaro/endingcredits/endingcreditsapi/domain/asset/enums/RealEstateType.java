package com.hanaro.endingcredits.endingcreditsapi.domain.asset.enums;

import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.code.status.ErrorStatus;
import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.exception.handler.AssetHandler;

public enum RealEstateType {
    APARTMENT("아파트"),
    VILLA("빌라"),
    OFFICETEL("오피스텔"),
    HOUSE("주택"),
    LAND("토지");

    private final String description;

    RealEstateType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static RealEstateType fromDescription(String description) {
        for (RealEstateType realEstateType : values()) {
            if (realEstateType.getDescription().equals(description)) {
                return realEstateType;
            }
        }
        throw new AssetHandler(ErrorStatus.REAL_ESTATE_TYPE_NOT_FOUND);
    }
}
