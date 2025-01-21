package com.hanaro.endingcredits.endingcreditsapi.domain.product.controller;

import com.hanaro.endingcredits.endingcreditsapi.domain.product.dto.PensionSavingsDetailResponseDto;
import com.hanaro.endingcredits.endingcreditsapi.domain.product.dto.PensionSavingsListResponseDto;
import com.hanaro.endingcredits.endingcreditsapi.domain.product.dto.PensionSavingsResponseDto;
import com.hanaro.endingcredits.endingcreditsapi.domain.product.entities.PensionSavingsEsEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.product.service.PensionSavingsService;
import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.ApiResponseEntity;
import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.code.status.ErrorStatus;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class PensionSavingsController {

    private final PensionSavingsService pensionSavingsService;

    @GetMapping("/pension-savings/all")
    @Operation(summary = "연금저축 상품 전체 목록 조회", description = "상품 연금저축 상품 목록을 조회합니다.")
    public ApiResponseEntity<List<PensionSavingsListResponseDto>> getAllPensionProductList() {
        List<PensionSavingsListResponseDto> responseDto = pensionSavingsService.getAllSavingsProductList();
        return ApiResponseEntity.onSuccess(responseDto);
    }

    @GetMapping("/pension-savings")
    @Operation(summary = "연금저축 상품 목록을 권역으로 조회", description = "파라미터로 전달받은 권역으로 상품 연금저축 상품 목록을 조회합니다.")
    public ApiResponseEntity<List<PensionSavingsListResponseDto>> getPensionProductListByAreaCode(@RequestParam int areaCode){
        List<PensionSavingsListResponseDto> responseDto = pensionSavingsService.getSavingsProductListByAreaCode(areaCode);
        return ApiResponseEntity.onSuccess(responseDto);
    }

    @GetMapping("/pension-savings/comparision/{productId}")
    @Operation(summary = "연금저축 상품 비교 상세 조회", description = "연금저축 상품을 비교하기 위한 상세를 상품 ID로 조회합니다.")
    public ApiResponseEntity<PensionSavingsResponseDto> getPensionProduct(@PathVariable(name = "productId") UUID productId){
        PensionSavingsResponseDto responseDto = pensionSavingsService.getSavingsProduct(productId);
        return ApiResponseEntity.onSuccess(responseDto);
    }

    @GetMapping("/pension-savings/search")
    @Operation(summary = "연금저축 상품 검색어로 조회", description = "상품명으로 상품을 조회합니다.")
    public ApiResponseEntity<List<PensionSavingsEsEntity>> searchProducts(
            @RequestParam String keyword,
            @RequestParam int areaCode) {
        try {
            List<PensionSavingsEsEntity> responseDto = pensionSavingsService.searchProducts(keyword, areaCode);

            if (responseDto.isEmpty()) {
                return ApiResponseEntity.onFailure(ErrorStatus.RECOMMEND_NOT_FOUND.getCode(), ErrorStatus.RECOMMEND_NOT_FOUND.getMessage(), null);
            }

            return ApiResponseEntity.onSuccess(responseDto);
        } catch (Exception e) {
            return ApiResponseEntity.onFailure(ErrorStatus.PRODUCT_NOT_FOUND.getCode(), ErrorStatus.PRODUCT_NOT_FOUND.getMessage(), null);
        }
    }

    @GetMapping("/pension-savings/detail/{productId}")
    @Operation(summary = "연금저축 상품 상세 조회", description = "연금저축 상품을 상세 조회합니다.")
    public ApiResponseEntity<PensionSavingsDetailResponseDto> getSavingsProductDetail(@PathVariable(name = "productId") UUID productId) {
        PensionSavingsDetailResponseDto responseDto = pensionSavingsService.getSavingsProductDetail(productId);
        return ApiResponseEntity.onSuccess(responseDto);
    }
}
