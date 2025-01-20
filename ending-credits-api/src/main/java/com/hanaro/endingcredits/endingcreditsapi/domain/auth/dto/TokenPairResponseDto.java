package com.hanaro.endingcredits.endingcreditsapi.domain.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenPairResponseDto {
    private String accessToken;
    private String refreshToken;
}
