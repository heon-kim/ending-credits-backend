package com.hanaro.endingcredits.endingcreditsapi.domain.asset.dto;

import com.hanaro.endingcredits.endingcreditsapi.domain.asset.enums.CurrencyCodeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VirtualAssetDto {
    private UUID virtualAssetId;      // 가상자산 ID
    private String exchangeName;      // 거래소 이름
    private String virtualAssetName;  // 가상자산 이름
    private BigDecimal currentPrice;  // 현재 가격
    private BigDecimal purchasePrice; // 구매 가격
    private BigDecimal profitRatio;   // 수익률
    private BigDecimal quantity;      // 보유 수량
    private BigDecimal totalValue;    // 총 평가 금액
    private CurrencyCodeType currencyCode; // 화폐
}

