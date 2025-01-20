package com.hanaro.endingcredits.endingcreditsapi.domain.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CerificationCodeDto {
    private String code;
    private long expiryTime;

    public boolean isExpired() {
        return System.currentTimeMillis() > expiryTime;
    }
}
