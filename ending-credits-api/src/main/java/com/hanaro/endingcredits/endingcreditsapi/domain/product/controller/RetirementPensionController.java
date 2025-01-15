package com.hanaro.endingcredits.endingcreditsapi.domain.product.controller;

import com.hanaro.endingcredits.endingcreditsapi.domain.product.dto.RetirementPensionProductSummaryDto;
import com.hanaro.endingcredits.endingcreditsapi.domain.product.entities.RetirementPensionProductEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.product.service.RetirementPensionService;
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
    public ResponseEntity<List<RetirementPensionProductSummaryDto>> getPensionProducts(
            @RequestParam int areaCode,
            @RequestParam int sysTypeCode) {
        List<RetirementPensionProductSummaryDto> productList = retirementPensionService.getPensionProducts(areaCode, sysTypeCode);
        return ResponseEntity.ok(productList);
    }

    @GetMapping("/annuity/{productId}")
    public ResponseEntity<?> getPensionProductById(@PathVariable UUID productId) {
        Optional<RetirementPensionProductEntity> product = retirementPensionService.getPensionProductById(productId);

        return product.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/annuity/all")
    public ResponseEntity<List<RetirementPensionProductSummaryDto>> getAllPensionProducts() {
        List<RetirementPensionProductSummaryDto> allProducts = retirementPensionService.getAllPensionProducts();
        return ResponseEntity.ok(allProducts);
    }
}
