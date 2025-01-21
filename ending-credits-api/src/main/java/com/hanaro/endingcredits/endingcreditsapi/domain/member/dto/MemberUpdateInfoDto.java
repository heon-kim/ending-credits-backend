package com.hanaro.endingcredits.endingcreditsapi.domain.member.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberUpdateInfoDto {
    private String phoneNumber;
    private String address;
}
