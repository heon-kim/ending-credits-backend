package com.hanaro.endingcredits.endingcreditsapi.domain.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class YieldDetailDto {
    // 수익률 추이
    private double dbEarnRate; // DB 장기수익률 해당 분기
    private double dbEarnRate3; // DB 장기수익률 3년
    private double dbEarnRate5; // DB 장기수익률 5년
    private double dbEarnRate7; // DB 장기수익률 7년
    private double dbEarnRate10; //DB 장기수익률 10년

    private double dcEarnRate; // DC 장기수익률 해당 분기
    private double dcEarnRate3; // DC 장기수익률 3년
    private double dcEarnRate5; // DC 장기수익률 5년
    private double dcEarnRate7; // DC 장기수익률 7년
    private double dcEarnRate10; // DC 장기수익률 10년

    private double irpEarnRate; // IRP 장기수익률 해당 분기
    private double irpEarnRate3; // IRP 장기수익률 3년
    private double irpEarnRate5; // IRP 장기수익률 5년
    private double irpEarnRate7; // IRP 장기수익률 7년
    private double irpEarnRate10; // IRP 장기수익률 10년
}