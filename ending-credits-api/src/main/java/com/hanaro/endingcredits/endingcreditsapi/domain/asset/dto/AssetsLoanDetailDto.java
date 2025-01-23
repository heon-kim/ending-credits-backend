package com.hanaro.endingcredits.endingcreditsapi.domain.asset.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class AssetsLoanDetailDto {
    private AssetsDetailDto assetsDetail;
    private List<LoanDetailDto> loan;
    private String loanTotal;
}
