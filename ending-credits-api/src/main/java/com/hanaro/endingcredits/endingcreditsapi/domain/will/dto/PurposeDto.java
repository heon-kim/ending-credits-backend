package com.hanaro.endingcredits.endingcreditsapi.domain.will.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PurposeDto {
    private Boolean status;
    private String createDate;
}
