package com.hanaro.endingcredits.endingcreditsapi.domain.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponseDto {
    private String name;
    private TokenPairResponseDto tokenPairResponseDto;
}
