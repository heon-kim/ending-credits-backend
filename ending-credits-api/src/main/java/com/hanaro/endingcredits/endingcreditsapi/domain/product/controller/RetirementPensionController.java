package com.hanaro.endingcredits.endingcreditsapi.domain.product.controller;

import com.hanaro.endingcredits.endingcreditsapi.domain.product.dto.RetirementPensionCompanySummaryDto;
import com.hanaro.endingcredits.endingcreditsapi.domain.product.dto.RetirementPensionDetailResponseDto;
import com.hanaro.endingcredits.endingcreditsapi.domain.product.dto.RetirementPensionFeeComparisonDto;
import com.hanaro.endingcredits.endingcreditsapi.domain.product.dto.SliceResponse;
import com.hanaro.endingcredits.endingcreditsapi.domain.product.entities.RetirementPensionSearchItems;
import com.hanaro.endingcredits.endingcreditsapi.domain.product.service.RetirementPensionService;
import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.ApiResponseEntity;
import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.code.status.ErrorStatus;
import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.exception.handler.FinanceHandler;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/product/annuity")
@RequiredArgsConstructor
public class RetirementPensionController {
    private final RetirementPensionService retirementPensionService;
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

    @GetMapping("/all")
    @Operation(summary = "퇴직연금 기업 전체 목록 조회", description = "퇴직연금 기업 전체 목록을 조회합니다.")
    public ApiResponseEntity<SliceResponse<RetirementPensionCompanySummaryDto>> getAllPensionProducts(@PageableDefault(size = 8) Pageable pageable) {
        try {
            SliceResponse<RetirementPensionCompanySummaryDto> allProducts = retirementPensionService.getAllCompany(pageable);
            return ApiResponseEntity.onSuccess(allProducts);
        } catch (Exception e) {
            return ApiResponseEntity.onFailure(ErrorStatus.YIELD_NOT_FOUND.getCode(), ErrorStatus.YIELD_NOT_FOUND.getMessage(), null);
        }
    }


    @GetMapping("/search")
    @Operation(summary = "퇴직연금 기업명 검색어로 조회", description = "기업명을 검색해서 기업을 조회합니다.")
    public ApiResponseEntity<List<RetirementPensionSearchItems>> searchProducts(
            @RequestParam String keyword) {
        try {
            List<RetirementPensionSearchItems> products = retirementPensionService.searchCompany(keyword);
            return ApiResponseEntity.onSuccess(products);
        } catch (Exception e) {
            return ApiResponseEntity.onFailure(ErrorStatus.SEARCH_NOT_FOUND.getCode(), ErrorStatus.SEARCH_NOT_FOUND.getMessage(), null);
        }
    }
}
