package com.hanaro.endingcredits.endingcreditsapi.domain.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RetirementPensionDetailResponseDto {
    private String company; // 기업명
    private String area; // 권역
    private Map<String, YieldDetailDto> earnRates; // 원리금 보장 여부별 수익률
}
