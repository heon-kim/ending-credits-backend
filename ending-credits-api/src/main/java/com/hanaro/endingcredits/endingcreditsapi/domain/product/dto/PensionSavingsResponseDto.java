package com.hanaro.endingcredits.endingcreditsapi.domain.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PensionSavingsResponseDto {
    private String area; // 권역
    private String company; // 은행명
    private String product; // 상품명
    private String productType; // 상품 유형
    private String rcvMethod; // 수령 기간
    private String feeType; // 수수료 구조
    private String sells; // 판매 여부 (진행/중단)
    private String withdraws; // 중도 해지 (가능/불가능)
    private String guarantees; // 원금 보장 (보장/비보장)

    // 납입원금
    private String currentBalance; // 현재
    private String previousYearBalance; // 과거 1년
    private String twoYearsAgoBalance; // 과거 2년
    private String threeYearsAgoBalance; // 과거 3년

    // 적립금
    private String currentReserve; // 현재
    private String previousYearReserve; // 과거 1년
    private String twoYearsAgoReserve; // 과거 2년
    private String threeYearsAgoReserve; // 과거 3년

    // 수익률
    private double currentEarnRate; // 현재
    private double previousYearEarnRate; // 과거 1년
    private double twoYearsAgoEarnRate; // 과거 2년
    private double threeYearsAgoEarnRate; // 과거 3년

    // 수수료율
    private double previousYearFeeRate; // 과거 1년
    private double twoYearsAgoFeeRate; // 과거 2년
    private double threeYearsAgoFeeRate; // 과거 3년
}