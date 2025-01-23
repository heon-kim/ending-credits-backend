package com.hanaro.endingcredits.endingcreditsapi.domain.asset.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BankAssetDto {
    private String bankName;
    private String assetType;
    private String accountName;
    private String accountNumber;
    private BigDecimal amount;
    private BigDecimal profitRate;
}
