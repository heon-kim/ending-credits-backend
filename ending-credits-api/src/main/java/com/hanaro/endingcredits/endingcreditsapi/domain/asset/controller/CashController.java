package com.hanaro.endingcredits.endingcreditsapi.domain.asset.controller;

import com.hanaro.endingcredits.endingcreditsapi.domain.asset.service.CashService;
import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.ApiResponseEntity;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/asset")
public class CashController {

    private final CashService cashService;

    @GetMapping("/cash")
    @Operation(summary= "현금 자산 조회", description = "보유하고 있는 현금 자산을 조회합니다.")
    public ApiResponseEntity<BigDecimal> getMemberCash(@AuthenticationPrincipal UUID memberId) {
        BigDecimal cash = cashService.getCashBalanceByMemberId(memberId);
        return ApiResponseEntity.onSuccess("현금 자산 조회에 성공하였습니다", cash);
    }
}