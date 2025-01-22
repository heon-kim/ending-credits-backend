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
    String area; // 권역
    String company; // 은행명
    String product; // 상품명
    String productType; // 상품 유형
    String rcvMethod; // 수령 기간
    String feeType; // 수수료 구조
    String sells; // 판매 여부 (진행/중단)
    String withdraws; // 중도 해지 (가능/불가능)
    String guarantees; // 원금 보장 (보장/비보장)

    // 납입원금
    int currentBalance; // 현재
    int previousYearBalance; // 과거 1년
    int twoYearsAgoBalance; // 과거 2년
    int threeYearsAgoBalance; // 과거 3년

    // 적립금
    int currentReserve; // 현재
    int previousYearReserve; // 과거 1년
    int twoYearsAgoReserve; // 과거 2년
    int threeYearsAgoReserve; // 과거 3년

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