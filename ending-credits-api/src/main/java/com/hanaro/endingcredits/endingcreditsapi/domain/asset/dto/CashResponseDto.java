package com.hanaro.endingcredits.endingcreditsapi.domain.asset.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CashResponseDto {
    private UUID id;
    private BigDecimal amount;
}
