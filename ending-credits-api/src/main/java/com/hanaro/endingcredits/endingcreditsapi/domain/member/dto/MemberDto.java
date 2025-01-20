package com.hanaro.endingcredits.endingcreditsapi.domain.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class MemberDto {
    private String identifier;
    private String password;
    private String simplePassword;
    private LoginType loginType;
    private LocalDate birthDate;
    private String phoneNumber;
    private String address;
    private String name;
    private String email;
    private boolean isActive;
}
