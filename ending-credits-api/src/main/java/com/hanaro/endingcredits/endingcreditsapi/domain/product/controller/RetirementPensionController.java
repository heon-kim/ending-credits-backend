package com.hanaro.endingcredits.endingcreditsapi.domain.product.controller;

import com.hanaro.endingcredits.endingcreditsapi.domain.product.dto.RetirementPensionProductSummaryDto;
import com.hanaro.endingcredits.endingcreditsapi.domain.product.entities.RetirementPensionEsEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.product.entities.RetirementPensionProductEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.product.service.RetirementPensionService;
import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.ApiResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class RetirementPensionController {
    private final RetirementPensionService retirementPensionService;

    @GetMapping("/annuity")
    public ApiResponseEntity<List<RetirementPensionProductSummaryDto>> getPensionProducts(
            @RequestParam int areaCode,
            @RequestParam int sysTypeCode) {
        try {
            List<RetirementPensionProductSummaryDto> productList = retirementPensionService.getPensionProducts(areaCode, sysTypeCode);
            return ApiResponseEntity.onSuccess(productList);
        } catch (Exception e) {
            return ApiResponseEntity.onFailure("PRODUCT4001", "상품 목록 조회 중 오류가 발생했습니다.", null);
        }
    }

    @GetMapping("/annuity/{productId}")
    public ApiResponseEntity<?> getPensionProductById(@PathVariable UUID productId) {
        Optional<RetirementPensionProductEntity> product = retirementPensionService.getPensionProductById(productId);

        if(product.isPresent()) {
            return ApiResponseEntity.onSuccess(product.get());
        } else {
            return ApiResponseEntity.onFailure("PRODUCT4002", "해당 상품을 찾을 수 없습니다.", null);
        }
    }

    @GetMapping("/search")
    public ApiResponseEntity<List<RetirementPensionEsEntity>> searchProducts(
            @RequestParam String keyword,
            @RequestParam int areaCode,
            @RequestParam int sysType) {
        try {
            List<RetirementPensionEsEntity> products = retirementPensionService.searchProducts(keyword, areaCode, sysType);
            return ApiResponseEntity.onSuccess(products);
        } catch (Exception e) {
            return ApiResponseEntity.onFailure("PRODUCT4003", "상품 검색 중 오류가 발생했습니다.", null);
        }
    }

    @GetMapping("/annuity/all")
    public ApiResponseEntity<List<RetirementPensionProductSummaryDto>> getAllPensionProducts() {
        try {
            List<RetirementPensionProductSummaryDto> allProducts = retirementPensionService.getAllPensionProducts();
            return ApiResponseEntity.onSuccess(allProducts);
        } catch (Exception e) {
            return ApiResponseEntity.onFailure("PRODUCT4004", "전체 상품 조회 중 오류가 발생했습니다.", null);
        }
    }
}
