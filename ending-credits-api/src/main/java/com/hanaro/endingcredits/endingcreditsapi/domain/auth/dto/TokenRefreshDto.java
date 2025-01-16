package com.hanaro.endingcredits.endingcreditsapi.domain.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenRefreshDto {
    private String refreshToken;
}
