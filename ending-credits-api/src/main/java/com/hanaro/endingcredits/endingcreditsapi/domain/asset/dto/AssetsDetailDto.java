package com.hanaro.endingcredits.endingcreditsapi.domain.asset.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AssetsDetailDto {
    private String bank;
    private String securityCompany;
    private String virtual;
    private String cash;
    private String realEstate;
    private String car;
    private String pension;
    private String assetTotal;
}

