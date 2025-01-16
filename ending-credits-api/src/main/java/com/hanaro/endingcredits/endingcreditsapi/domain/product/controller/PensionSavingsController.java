package com.hanaro.endingcredits.endingcreditsapi.domain.product.controller;

import com.hanaro.endingcredits.endingcreditsapi.domain.product.dto.PensionSavingsListResponseDto;
import com.hanaro.endingcredits.endingcreditsapi.domain.product.dto.PensionSavingsResponseDto;
import com.hanaro.endingcredits.endingcreditsapi.domain.product.service.PensionSavingsService;
import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.ApiResponseEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.models.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class PensionSavingsController {

    private final PensionSavingsService pensionSavingsService;

    @GetMapping("/pension-savings")
    @Operation(summary = "연금저축 상품 목록 조회",description = "상품 연금저축 상품 목록을 조회합니다.")
    public ApiResponseEntity<List<PensionSavingsListResponseDto>> getPensionProductList(@RequestParam int areaCode){
        List<PensionSavingsListResponseDto> responseDto = pensionSavingsService.getSavingsProductList(areaCode);
        return ApiResponseEntity.onSuccess(responseDto);
    }

    @GetMapping("/pension-savings/{productId}")
    @Operation(summary = "연금저축 상품 상세 조회",description = "상품 ID로 연금저축 상품을 조회합니다.")
    public ApiResponseEntity<PensionSavingsResponseDto> getPensionProduct(@PathVariable(name = "productId") UUID productId){
        PensionSavingsResponseDto responseDto = pensionSavingsService.getSavingsProduct(productId);
        return ApiResponseEntity.onSuccess(responseDto);
    }
}
