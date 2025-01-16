package com.hanaro.endingcredits.endingcreditsapi.domain.product.controller;

import ch.qos.logback.core.status.ErrorStatus;
import com.hanaro.endingcredits.endingcreditsapi.domain.product.dto.PensionSavingsResponseDto;
import com.hanaro.endingcredits.endingcreditsapi.domain.product.entities.Strategy;
import com.hanaro.endingcredits.endingcreditsapi.domain.product.service.RecommendService;
import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.ApiResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class RecommendController {
    private final RecommendService recommendService;
    @GetMapping("/recommend")
    public ApiResponseEntity<PensionSavingsResponseDto> recommendProduct(@RequestParam String strategyName) {
        try {
            // 문자열을 Strategy Enum으로 변환
            Strategy strategy = Strategy.fromDescription(strategyName);

            // 추천된 상품 반환
            PensionSavingsResponseDto recommendedProduct = recommendService.recommendProduct(strategy);
            return ApiResponseEntity.onSuccess(recommendedProduct);

        } catch (IllegalArgumentException e) {
            // 유효하지 않은 전략명 처리
            return ApiResponseEntity.onFailure("PRODUCT4002", "추천상품 결과가 없습니다.", null);
        }
    }
}
