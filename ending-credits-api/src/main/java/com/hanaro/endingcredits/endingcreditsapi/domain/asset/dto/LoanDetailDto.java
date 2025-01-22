package com.hanaro.endingcredits.endingcreditsapi.domain.asset.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoanDetailDto {
    private String totalAmount;
    private String loanAmount;
    private String expiryRemainDay;
    private String accountName;
    private String accountNumber;
    private String bankName;
}
