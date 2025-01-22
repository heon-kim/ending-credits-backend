package com.hanaro.endingcredits.endingcreditsapi.domain.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RetirementPensionDetailResponseDto {
    String company; // 기업명
    String area; // 권역
    String division; // 원리금 보장 여부 (보장/비보장)

    // 수익률 추이
    double dbEarnRate; // DB 장기수익률 해당 분기
    double dbEarnRate3; // DB 장기수익률 3년
    double dbEarnRate5; // DB 장기수익률 5년
    double dbEarnRate7; // DB 장기수익률 7년
    double dbEarnRate10; //DB 장기수익률 10년

    double dcEarnRate; // DC 장기수익률 해당 분기
    double dcEarnRate3; // DC 장기수익률 3년
    double dcEarnRate5; // DC 장기수익률 5년
    double dcEarnRate7; // DC 장기수익률 7년
    double dcEarnRate10; // DC 장기수익률 10년

    double irpEarnRate; // IRP 장기수익률 해당 분기
    double irpEarnRate3; // IRP 장기수익률 3년
    double irpEarnRate5; // IRP 장기수익률 5년
    double irpEarnRate7; // IRP 장기수익률 7년
    double irpEarnRate10; // IRP 장기수익률 10년
}
