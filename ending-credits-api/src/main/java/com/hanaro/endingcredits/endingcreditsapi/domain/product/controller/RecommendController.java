package com.hanaro.endingcredits.endingcreditsapi.domain.product.controller;

import com.hanaro.endingcredits.endingcreditsapi.domain.product.dto.PensionSavingsResponseComparisonDto;
import com.hanaro.endingcredits.endingcreditsapi.domain.product.dto.RecommendProductResponseDto;
import com.hanaro.endingcredits.endingcreditsapi.domain.product.entities.Strategy;
import com.hanaro.endingcredits.endingcreditsapi.domain.product.service.RecommendService;
import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.ApiResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class RecommendController {
    private final RecommendService recommendService;
    @GetMapping("/recommend")
    public ApiResponseEntity<List<RecommendProductResponseDto>> recommendProduct() {
        try {
            List<RecommendProductResponseDto> recommendedProduct = recommendService.recommendProduct();
            return ApiResponseEntity.onSuccess(recommendedProduct);
        } catch (IllegalArgumentException e) {
            // 유효하지 않은 전략명 처리
            return ApiResponseEntity.onFailure("PRODUCT4002", "추천상품 결과가 없습니다.", null);
        }
    }
}
