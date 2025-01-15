package com.hanaro.endingcredits.endingcreditsapi.domain.product.controller;

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
    public List<RetirementPensionProductEntity> getPensionProducts(
            @RequestParam int areaCode,
            @RequestParam int sysTypeCode) {
        return retirementPensionService.getPensionProducts(areaCode, sysTypeCode);
    }

    @GetMapping("/annuity/{productId}")
    public ResponseEntity<?> getPensionProductById(@PathVariable UUID productId) {
        Optional<RetirementPensionProductEntity> product = retirementPensionService.getPensionProductById(productId);

        return product.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
