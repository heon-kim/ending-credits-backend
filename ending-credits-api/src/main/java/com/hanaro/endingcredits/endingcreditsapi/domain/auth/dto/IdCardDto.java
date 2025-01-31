package com.hanaro.endingcredits.endingcreditsapi.domain.auth.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IdCardDto {
    private String name;
    private String address;
    private String idNumber;
}
