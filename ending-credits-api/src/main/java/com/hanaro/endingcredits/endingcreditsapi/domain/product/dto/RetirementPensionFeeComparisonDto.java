package com.hanaro.endingcredits.endingcreditsapi.domain.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RetirementPensionFeeComparisonDto {
    private UUID companyId; // 기업 ID
    private String area; // 권역
    private String company; // 기업명
    private double dbTotalCostRate; // DB 총비용부담률
    private int dbTotalFee; // DB 수수료 합계
    private int dbOprtMngFee; // DB 운용관리 수수료
    private int dbAsstMngFee; // DB 자산관리 수수료
    private int dbFundTotalCost; // DB 펀드 총비용
    private double dcTotalCostRate; // DC 총비용부담률
    private int dcTotalFee; // DC 수수료 합계
    private int dcOprtMngFee; // DC 운용관리 수수료
    private int dcAsstMngFee;  // DC 자산관리 수수료
    private int dcFundTotalCost;	//DC 펀드 총비용
    private double irpTotalCostRate;	//	IRP 총비용부담률
    private int irpTotalFee;	//	IRP 수수료 합계
    private int irpOprtMngFee;	//	IRP 운용관리 수수료
    private int irpAsstMngFee;	//	IRP 자산관리 수수료
    private int irpFundTotalCost;	//	IRP 펀드 총비용
}
