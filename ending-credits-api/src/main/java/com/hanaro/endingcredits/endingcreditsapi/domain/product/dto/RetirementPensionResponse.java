package com.hanaro.endingcredits.endingcreditsapi.domain.product.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RetirementPensionResponse {
    private String code;
    private String message;
    private int count;
    private List<CompanyDto> list;
}
