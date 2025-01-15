package com.hanaro.endingcredits.endingcreditsapi.domain.member.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class MemberInfoDto {
    private LocalDate birthDate;
    private String phoneNumber;
    private String address;
    private String name;
}
