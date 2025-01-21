package com.hanaro.endingcredits.endingcreditsapi.domain.asset.controller;

import com.hanaro.endingcredits.endingcreditsapi.domain.asset.dto.*;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.service.EtcDetailService;
import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.ApiResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public ResponseEntity<ApiResponseEntity<List<CarDetailDto>>> getConnectedCars(
            @AuthenticationPrincipal UUID memberId) {
        List<CarDetailDto> connectedCars = etcDetailService.getConnectedCars(memberId);
        return ResponseEntity.ok(ApiResponseEntity.onSuccess("자동차 상세 조회 성공", connectedCars));
    }

    /**
     * 연결된 부동산 상세 조회
     */
    @GetMapping("/real-estates")
    public ResponseEntity<ApiResponseEntity<List<RealEstateDetailDto>>> getConnectedRealEstates(
            @AuthenticationPrincipal UUID memberId) {
        List<RealEstateDetailDto> connectedRealEstates = etcDetailService.getConnectedRealEstates(memberId);
        return ResponseEntity.ok(ApiResponseEntity.onSuccess("부동산 상세 조회 성공", connectedRealEstates));
    }

    /**
     * 연결된 연금 상세 조회
     */
    @GetMapping("/pensions")
    public ResponseEntity<ApiResponseEntity<List<PensionDetailDto>>> getConnectedPensions(
            @AuthenticationPrincipal UUID memberId) {
        List<PensionDetailDto> connectedPensions = etcDetailService.getConnectedPensions(memberId);
        return ResponseEntity.ok(ApiResponseEntity.onSuccess("연금 상세 조회 성공", connectedPensions));
    }

    /**
     * 자동차 자산 추가
     */
    @PostMapping("/car")
    public ResponseEntity<ApiResponseEntity<CarDetailDto>> addCar(@RequestBody AddCarRequest request) {
        CarDetailDto addedCar = etcDetailService.addCar(
                request.getMemberId(),
                request.getModel(),
                request.getCarNumber(),
                request.getPurchasePrice(),
                request.getCurrentPurchasePrice(),
                request.getMileage(),
                request.getYear()
        );
        return ResponseEntity.ok(ApiResponseEntity.onSuccess("자동차 자산 추가 성공", addedCar));
    }

    /**
     * 부동산 자산 추가
     */
    @PostMapping("/real-estate")
    public ResponseEntity<ApiResponseEntity<RealEstateDetailDto>> addRealEstate(@RequestBody AddRealEstateRequest request) {
        RealEstateDetailDto addedRealEstate = etcDetailService.addRealEstate(
                request.getMemberId(),
                request.getName(),
                request.getAddress(),
                request.getPurchasePrice(),
                request.getCurrentPrice()
        );
        return ResponseEntity.ok(ApiResponseEntity.onSuccess("부동산 자산 추가 성공", addedRealEstate));
    }

    /**
     * 자동차 자산 수정
     */
    @PutMapping("/car/{carId}")
    public ResponseEntity<ApiResponseEntity<CarDetailDto>> updateCarPurchasePrice(
            @PathVariable UUID carId,
            @RequestBody Long newPurchasePrice) {
        CarDetailDto updatedCar = etcDetailService.updateCarPurchasePrice(carId, newPurchasePrice);
        return ResponseEntity.ok(ApiResponseEntity.onSuccess("자동차 자산 수정 성공", updatedCar));
    }

    /**
     * 부동산 자산 수정
     */
    @PutMapping("/real-estate/{realEstateId}")
    public ResponseEntity<ApiResponseEntity<RealEstateDetailDto>> updateRealEstatePurchasePrice(
            @PathVariable UUID realEstateId,
            @RequestBody Long newPurchasePrice) {
        RealEstateDetailDto updatedRealEstate = etcDetailService.updateRealEstatePurchasePrice(realEstateId, newPurchasePrice);
        return ResponseEntity.ok(ApiResponseEntity.onSuccess("부동산 자산 수정 성공", updatedRealEstate));
    }

}
