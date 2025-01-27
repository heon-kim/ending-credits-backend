package com.hanaro.endingcredits.endingcreditsapi.domain.asset.controller;

import com.hanaro.endingcredits.endingcreditsapi.domain.asset.dto.CashResponseDto;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.service.CashService;
import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.ApiResponseEntity;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/asset")
public class CashController {

    private final CashService cashService;

    @GetMapping("/cash")
    @Operation(summary= "현금 자산 조회", description = "보유하고 있는 현금 자산을 조회합니다.")
    public ApiResponseEntity<CashResponseDto> getMemberCash(@AuthenticationPrincipal UUID memberId) {
        CashResponseDto responseDto = cashService.getCashBalanceByMemberId(memberId);
        return ApiResponseEntity.onSuccess("현금 자산 조회에 성공하였습니다", responseDto);
    }

    @PatchMapping("/cash")
    @Operation(summary= "현금 자산 설정", description = "보유하고 있는 현금 자산을 설정하거나 수정합니다.")
    public ApiResponseEntity<BigDecimal> setCash(@AuthenticationPrincipal UUID memberId, @RequestBody Map<String, BigDecimal> body) {
        BigDecimal amount = body.get("amount");
        cashService.updateCashAmount(memberId, amount);
        return ApiResponseEntity.onSuccess("보유 중인 현금 자산을 등록하였습니다.", amount);
    }
}