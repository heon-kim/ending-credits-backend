package com.hanaro.endingcredits.endingcreditsapi.domain.product.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.AllArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class RetirementPensionResponse {
    private final String code;
    private final String message;
    private final int count;
    private final List<RetirementPensionProductDto> list;
}
