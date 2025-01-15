package com.hanaro.endingcredits.endingcreditsapi.domain.product.controller;

import com.hanaro.endingcredits.endingcreditsapi.domain.product.entities.RetirementPensionProductEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.product.service.RetirementPensionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/retirement-pension")
@RequiredArgsConstructor
public class RetirementPensionController {
    private final RetirementPensionService retirementPensionService;

    /**
     * 퇴직연금상품 데이터 저장 (API 호출 및 DB 저장)
     */
    @PostMapping("/save")
    public ResponseEntity<String> savePensionData(
            @RequestParam int areaCode,
            @RequestParam int sysTypeCode,
            @RequestParam String reportDate) {
        retirementPensionService.savePensionData(areaCode, sysTypeCode, reportDate);
        return ResponseEntity.ok("퇴직연금 상품 데이터 저장 완료");
    }

//
}
/**
 //     * 모든 퇴직연금상품 조회
 //     */
//    @GetMapping("/all")
//    public ResponseEntity<List<RetirementPensionProductEntity>> getAllPensionProducts() {
//        return ResponseEntity.ok(retirementPensionService.getAllPensionProducts());
//    }
