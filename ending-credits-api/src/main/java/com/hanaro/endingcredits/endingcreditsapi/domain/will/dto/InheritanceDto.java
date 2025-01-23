package com.hanaro.endingcredits.endingcreditsapi.domain.will.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class InheritanceDto {
    private String type;
    private String subType;
    private String financialInstitution;
    private String asset;
    private BigDecimal amount;
    private List<AncestorDto> ancestors;
}
