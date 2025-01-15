package com.hanaro.endingcredits.endingcreditsapi.domain.product.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor
public class RetirementPensionProductDto {
    private final String company;
    private final String product;
    private final String applyTerm;
    private final String checkDate;
    private final int contractTerm;
    private final BigDecimal contractRate;
}
