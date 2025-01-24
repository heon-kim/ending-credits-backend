package com.hanaro.endingcredits.endingcreditsapi.domain.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PensionSavingsEstimateDto {
    private int expectedProfit; // 예상 수익액
    private int annualAdditionalUsage; // 연간 추가 사용 금액
    private int monthlyAdditionalUsage; // 월간 추가 사용 금액
    private double expectedEarnRate; // 예상 수익률
}
