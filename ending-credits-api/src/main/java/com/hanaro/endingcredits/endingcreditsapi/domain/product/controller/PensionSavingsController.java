package com.hanaro.endingcredits.endingcreditsapi.domain.product.controller;

import com.hanaro.endingcredits.endingcreditsapi.domain.product.dto.*;
import com.hanaro.endingcredits.endingcreditsapi.domain.product.entities.PensionSavingsSearchItems;
import com.hanaro.endingcredits.endingcreditsapi.domain.product.service.PensionSavingsService;
import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.ApiResponseEntity;
import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.code.status.ErrorStatus;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product/pension-savings")
public class PensionSavingsController {

    private final PensionSavingsService pensionSavingsService;

    @GetMapping("/all")
    @Operation(summary = "연금저축 상품 전체 목록 조회", description = "상품 연금저축 상품 목록을 조회합니다.")
    public ApiResponseEntity<SliceResponse<PensionSavingsListResponseDto>> getAllPensionProductList(@PageableDefault(size = 8) Pageable pageable) {
        SliceResponse<PensionSavingsListResponseDto> responseDto = pensionSavingsService.getAllSavingsProductList(pageable);
        return ApiResponseEntity.onSuccess(responseDto);
    }

    @GetMapping
    @Operation(summary = "연금저축 상품 목록을 권역으로 조회", description = "파라미터로 전달받은 권역으로 상품 연금저축 상품 목록을 조회합니다.")
    public ApiResponseEntity<List<PensionSavingsListResponseDto>> getPensionProductListByAreaCode(@RequestParam int areaCode){
        List<PensionSavingsListResponseDto> responseDto = pensionSavingsService.getSavingsProductListByAreaCode(areaCode);
        return ApiResponseEntity.onSuccess(responseDto);
    }

    @GetMapping("/comparison/{productId}")
    @Operation(summary = "연금저축 상품 비교 상세 조회", description = "연금저축 상품을 비교하기 위한 상세를 상품 ID로 조회합니다.")
    public ApiResponseEntity<PensionSavingsResponseComparisonDto> getPensionProduct(@PathVariable(name = "productId") UUID productId){
        PensionSavingsResponseComparisonDto responseDto = pensionSavingsService.getSavingsProduct(productId);
        return ApiResponseEntity.onSuccess(responseDto);
    }

    @GetMapping("/search")
    @Operation(summary = "연금저축 상품 검색어로 조회", description = "상품명으로 상품을 조회합니다.")
    public ApiResponseEntity<List<PensionSavingsSearchItems>> searchProducts(
            @RequestParam String keyword,
            @RequestParam int areaCode) {
        try {
            List<PensionSavingsSearchItems> responseDto = pensionSavingsService.searchProducts(keyword, areaCode);

            if (responseDto.isEmpty()) {
                return ApiResponseEntity.onFailure(ErrorStatus.RECOMMEND_NOT_FOUND.getCode(), ErrorStatus.RECOMMEND_NOT_FOUND.getMessage(), null);
            }

            return ApiResponseEntity.onSuccess(responseDto);
        } catch (Exception e) {
            return ApiResponseEntity.onFailure(ErrorStatus.PRODUCT_NOT_FOUND.getCode(), ErrorStatus.PRODUCT_NOT_FOUND.getMessage(), null);
        }
    }

    @GetMapping("/detail/{productId}")
    @Operation(summary = "연금저축 상품 상세 조회", description = "연금저축 상품을 상세 조회합니다.")
    public ApiResponseEntity<PensionSavingsDetailResponseDto> getSavingsProductDetail(@PathVariable(name = "productId") UUID productId) {
        PensionSavingsDetailResponseDto responseDto = pensionSavingsService.getSavingsProductDetail(productId);
        return ApiResponseEntity.onSuccess(responseDto);
    }

    @GetMapping("/detail/hana")
    @Operation(summary = "연금저축 하나은행 상품 조회", description = "하나은행 연금저축 상품들을 조회합니다.")
    public ApiResponseEntity<List<PensionSavingsListResponseDto>> getHanaPensionProductDetail() {
        List<PensionSavingsListResponseDto> responseDto = pensionSavingsService.getHanaSavingsProductList("하나은행");
        return ApiResponseEntity.onSuccess(responseDto);
    }

    @GetMapping("/calculate/{productId}")
    @Operation(summary = "수익률 계산", description = "투자 금액, 기간에 따른 수익 데이터를 계산해서 조회합니다.")
    public ApiResponseEntity<PensionSavingsEstimateDto> calculateProfit(@PathVariable UUID productId) {
        PensionSavingsEstimateDto responseDto = pensionSavingsService.calculateProfit(productId);
        return ApiResponseEntity.onSuccess(responseDto);
    }
}
