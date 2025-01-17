package com.hanaro.endingcredits.endingcreditsapi.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum LoginType {
    NORMAL,  // 일반 로그인
    KAKAO;   // 카카오 로그인

    @JsonCreator
    public static LoginType fromValue(String value) {
        try {
            return LoginType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }

    @JsonValue
    public String toValue() {
        return this.name().toLowerCase();
    }
}