package com.hanaro.endingcredits.endingcreditsapi.domain.asset.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AssetsWishDetailDto {
    private AssetsDetailDto assetsDetail;
    private String wishFund;
}

