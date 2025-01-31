package com.hanaro.endingcredits.endingcreditsapi.domain.asset.dto;

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
public class SecuritiesAssetDto {
    private UUID securitiesAssetId;       // 증권 자산 ID
    private String securitiesCompanyName; // 증권사 이름
    private String stockName;             // 주식 이름
    private String accountNumber;         // 계좌 번호
    private BigDecimal amount;            // 잔액
    private BigDecimal profitRate;        // 수익률
    private String currencyCode;          // 통화 코드 (KRW, USD)
}

