package com.hanaro.endingcredits.endingcreditsapi.domain.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PensionSavingsDetailResponseDto {
    String productName; // 상품명
    String productArea; // 권역
    String campany; // 기업명
    String productType; // 상품 유형
    String withdraws; // 중도 해지 (가능/불가능)

    // 수익률
    double currentEarnRate; // 현재
    double previousYearEarnRate; // 과거 1년
    double twoYearsAgoEarnRate; // 과거 2년
    double threeYearsAgoEarnRate; // 과거 3년

    // 수수료율
    double previousYearFeeRate; // 과거 1년
    double twoYearsAgoFeeRate; // 과거 2년
    double threeYearsAgoFeeRate; // 과거 3년
}
