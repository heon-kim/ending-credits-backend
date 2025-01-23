package com.hanaro.endingcredits.endingcreditsapi.domain.product.controller;

import com.hanaro.endingcredits.endingcreditsapi.domain.product.dto.PensionSavingsResponseComparisonDto;
import com.hanaro.endingcredits.endingcreditsapi.domain.product.dto.RetirementPensionCompanySummaryDto;
import com.hanaro.endingcredits.endingcreditsapi.domain.product.dto.RetirementPensionDetailResponseDto;
import com.hanaro.endingcredits.endingcreditsapi.domain.product.dto.RetirementPensionFeeComparisonDto;
import com.hanaro.endingcredits.endingcreditsapi.domain.product.entities.RetirementPensionEsEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.product.service.RetirementPensionService;
import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.ApiResponseEntity;
import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.code.status.ErrorStatus;
import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.exception.handler.FinanceHandler;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/product/annuity")
@RequiredArgsConstructor
public class RetirementPensionController {
    private final RetirementPensionService retirementPensionService;

//    @GetMapping("/annuity")
//    public ApiResponseEntity<List<RetirementPensionProductSummaryDto>> getPensionProducts(
//            @RequestParam int areaCode,
//            @RequestParam int sysTypeCode) {
//        try {
//            List<RetirementPensionProductSummaryDto> productList = retirementPensionService.getPensionProducts(areaCode, sysTypeCode);
//            return ApiResponseEntity.onSuccess(productList);
//        } catch (Exception e) {
//            return ApiResponseEntity.onFailure("PRODUCT4001", "상품 목록 조회 중 오류가 발생했습니다.", null);
//        }
//    }

//    @GetMapping("/annuity/{productId}")
//    public ApiResponseEntity<?> getPensionProductById(@PathVariable UUID productId) {
//        Optional<RetirementPensionYieldEntity> product = retirementPensionService.getPensionProductById(productId);
//
//        if(product.isPresent()) {
//            return ApiResponseEntity.onSuccess(product.get());
//        } else {
//            return ApiResponseEntity.onFailure("PRODUCT4002", "해당 상품을 찾을 수 없습니다.", null);
//        }
//    }

    @GetMapping("/comparison/{companyId}")
    @Operation(summary = "퇴직연금 비교 상세 조회", description = "퇴직연금 기업 비교하기 위한 상세를 기업 ID로 조회합니다.")
    public ApiResponseEntity<RetirementPensionFeeComparisonDto> getPensionComparisonDetail(@PathVariable UUID companyId) {
        try {
            RetirementPensionFeeComparisonDto responseDto = retirementPensionService.getPensionComparisonDetail(companyId);
            return ApiResponseEntity.onSuccess(responseDto);
        } catch (FinanceHandler e) {
            return ApiResponseEntity.onFailure(ErrorStatus.FEE_DETAILS_NOT_FOUND.getCode(), ErrorStatus.FEE_DETAILS_NOT_FOUND.getMessage(), null);
        }
    }

    @GetMapping("/detail/{companyId}")
    @Operation(summary = "퇴직연금 상세 수익률 조회", description = "퇴직연금 기업명 ID로 수익률을 상세 조회합니다.")
    public ApiResponseEntity<RetirementPensionDetailResponseDto> getPensionProductDetailById(@PathVariable UUID companyId) {
        try {
            RetirementPensionDetailResponseDto responseDto = retirementPensionService.getPensionProductDetailById(companyId);
            return ApiResponseEntity.onSuccess(responseDto);
        } catch (FinanceHandler e) {
            return ApiResponseEntity.onFailure(ErrorStatus.YIELD_NOT_FOUND.getCode(), ErrorStatus.YIELD_NOT_FOUND.getMessage(), null);
        }
    }

//    @GetMapping("/search")
//    public ApiResponseEntity<List<RetirementPensionEsEntity>> searchProducts(
//            @RequestParam String keyword,
//            @RequestParam int areaCode,
//            @RequestParam int sysType) {
//        try {
//            List<RetirementPensionEsEntity> products = retirementPensionService.searchProducts(keyword, areaCode, sysType);
//            return ApiResponseEntity.onSuccess(products);
//        } catch (Exception e) {
//            return ApiResponseEntity.onFailure("PRODUCT4003", "상품 검색 중 오류가 발생했습니다.", null);
//        }
//    }

    @GetMapping("/all")
    @Operation(summary = "퇴직연금 기업 전체 목록 조회", description = "퇴직연금 기업 전체 목록을 조회합니다.")
    public ApiResponseEntity<List<RetirementPensionCompanySummaryDto>> getAllPensionProducts() {
        try {
            List<RetirementPensionCompanySummaryDto> allProducts = retirementPensionService.getAllCompany();
            return ApiResponseEntity.onSuccess(allProducts);
        } catch (Exception e) {
            return ApiResponseEntity.onFailure(ErrorStatus.YIELD_NOT_FOUND.getCode(), ErrorStatus.YIELD_NOT_FOUND.getMessage(), null);
        }
    }
}
