package com.hanaro.endingcredits.endingcreditsapi.domain.product.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDto {
    private String company;
    private String area;
    private List<Map<String, Object>> list;
}
