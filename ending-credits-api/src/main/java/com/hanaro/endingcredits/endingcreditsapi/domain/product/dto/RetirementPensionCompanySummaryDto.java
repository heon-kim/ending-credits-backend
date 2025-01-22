package com.hanaro.endingcredits.endingcreditsapi.domain.product.dto;

import lombok.Getter;

import java.util.UUID;

@Getter
public class RetirementPensionProductSummaryDto {
    private final UUID productId;
    private final String productName;

    public RetirementPensionProductSummaryDto(UUID productId, String productName) {
        this.productId = productId;
        this.productName = productName;
    }
}
