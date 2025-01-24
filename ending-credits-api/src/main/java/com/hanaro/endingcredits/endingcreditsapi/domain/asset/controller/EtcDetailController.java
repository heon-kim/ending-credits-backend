package com.hanaro.endingcredits.endingcreditsapi.domain.asset.controller;

import com.hanaro.endingcredits.endingcreditsapi.domain.asset.dto.*;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.service.EtcDetailService;
import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.ApiResponseEntity;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/asset")
@RequiredArgsConstructor
public class EtcDetailController {
    private final EtcDetailService etcDetailService;

    /**
     * 연결된 자동차 상세 조회
     */
    @GetMapping("/cars")
    @Operation(summary= "자동차 자산 상세 조회", description = "보유하고 있는 자동차 자산을 보여줍니다.")
    public ApiResponseEntity<List<CarDetailDto>> getConnectedCars(
            @AuthenticationPrincipal UUID memberId) {
        List<CarDetailDto> connectedCars = etcDetailService.getConnectedCars(memberId);
        return ApiResponseEntity.onSuccess("자동차 상세 조회 성공", connectedCars);
    }

    /**
     * 연결된 부동산 상세 조회
     */
    @GetMapping("/real-estates")
    @Operation(summary= "부동산 자산 상세 조회", description = "보유하고 있는 부동산 자산을 보여줍니다.")
    public ApiResponseEntity<List<RealEstateDetailDto>> getConnectedRealEstates(
            @AuthenticationPrincipal UUID memberId) {
        List<RealEstateDetailDto> connectedRealEstates = etcDetailService.getConnectedRealEstates(memberId);
        return ApiResponseEntity.onSuccess("부동산 상세 조회 성공", connectedRealEstates);
    }

    /**
     * 연결된 연금 상세 조회
     */
    @GetMapping("/pensions")
    @Operation(summary= "연금 자산 상세 조회", description = "보유하고 있는 연금 자산을 보여줍니다.")
    public ApiResponseEntity<List<PensionDetailDto>> getConnectedPensions(
            @AuthenticationPrincipal UUID memberId) {
        List<PensionDetailDto> connectedPensions = etcDetailService.getConnectedPensions(memberId);
        return ApiResponseEntity.onSuccess("연금 상세 조회 성공", connectedPensions);
    }

    /**
     * 자동차 자산 추가
     */
    @PostMapping("/car")
    @Operation(summary= "자동차 자산 추가", description = "자동차 자산을 하나 추가합니다.")
    public ApiResponseEntity<CarDetailDto> addCar(
            @AuthenticationPrincipal UUID memberId,
            @RequestBody AddCarRequest request) {
        CarDetailDto addedCar = etcDetailService.addCar(
                memberId,
                request.getModel(),
                request.getCarNumber(),
                request.getPurchasePrice(),
                request.getCurrentPurchasePrice(),
                request.getMileage(),
                request.getYear()
        );
        return ApiResponseEntity.onSuccess("자동차 자산 추가 성공", addedCar);
    }

    /**
     * 부동산 자산 추가
     */
    @PostMapping("/real-estate")
    @Operation(summary= "부동산 자산 추가", description = "부동산 자산을 하나 추가합니다.")
    public ApiResponseEntity<RealEstateDetailDto> addRealEstate(
            @AuthenticationPrincipal UUID memberId,
            @RequestBody AddRealEstateRequest request) {
        RealEstateDetailDto addedRealEstate = etcDetailService.addRealEstate(
                memberId,
                request.getName(),
                request.getAddress(),
                request.getPurchasePrice(),
                request.getCurrentPrice()
        );
        return ApiResponseEntity.onSuccess("부동산 자산 추가 성공", addedRealEstate);
    }

    /**
     * 자동차 자산 수정
     */
    @PutMapping("/car/{carId}")
    @Operation(summary= "자동차 자산 가격 수정", description = "자동차 자산의 현재시세 가격을 수정합니다.")
    public ApiResponseEntity<CarDetailDto> updateCarPurchasePrice(
            @PathVariable UUID carId,
            @RequestBody Map<String, Long> body) {
        Long newPurchasePrice = body.get("newPurchasePrice");
        CarDetailDto updatedCar = etcDetailService.updateCarPurchasePrice(carId, newPurchasePrice);
        return ApiResponseEntity.onSuccess("자동차 자산 수정 성공", updatedCar);
    }

    /**
     * 부동산 자산 수정
     */
    @PutMapping("/real-estate/{realEstateId}")
    @Operation(summary= "부동산 자산 가격 수정", description = "부동산 자산의 현재시세 가격을 수정합니다.")
    public ApiResponseEntity<RealEstateDetailDto> updateRealEstatePurchasePrice(
            @PathVariable UUID realEstateId,
            @RequestBody Map<String, Long> body) {
        Long newPurchasePrice = body.get("newPurchasePrice");
        RealEstateDetailDto updatedRealEstate = etcDetailService.updateRealEstatePurchasePrice(realEstateId, newPurchasePrice);
        return ApiResponseEntity.onSuccess("부동산 자산 수정 성공", updatedRealEstate);
    }

}
