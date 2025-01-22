package com.hanaro.endingcredits.endingcreditsapi.domain.product.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
public class CompanyDto {
    private final String company;
    private final String area;
    private final List<Map<String, Object>> list;
}
