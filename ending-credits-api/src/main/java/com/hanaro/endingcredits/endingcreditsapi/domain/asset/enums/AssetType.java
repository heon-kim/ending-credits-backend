package com.hanaro.endingcredits.endingcreditsapi.domain.asset.enums;

import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.code.status.ErrorStatus;
import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.exception.handler.AssetHandler;

public enum AssetType {
    DEPOSIT("예금"),
    FUND("펀드"),
    TRUST("신탁"),
    SECURITIES("증권"),
    VIRTUAL_ASSET("가상자산"),
    CASH("현금"),
    REAL_ESTATE("부동산"),
    CAR("자동차"),
    PENSION("연금");

    private final String description;

    AssetType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static AssetType fromDescription(String description) {
        for (AssetType assetType : values()) {
            if (assetType.getDescription().equals(description)) {
                return assetType;
            }
        }
        throw new AssetHandler(ErrorStatus.ASSET_TYPE_NOT_FOUND);
    }
}
