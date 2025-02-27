package com.hanaro.endingcredits.endingcreditsapi.domain.product.service;

import com.hanaro.endingcredits.endingcreditsapi.domain.product.dto.PensionSavingsResponseComparisonDto;
import com.hanaro.endingcredits.endingcreditsapi.domain.product.dto.RecommendProductResponseDto;
import com.hanaro.endingcredits.endingcreditsapi.domain.product.entities.PensionSavingsProductEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.product.entities.ProductDetailEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.product.entities.Strategy;
import com.hanaro.endingcredits.endingcreditsapi.domain.product.repository.jpa.PensionSavingsJpaRepository;
import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.code.status.ErrorStatus;
import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.exception.handler.FinanceHandler;
import org.springframework.transaction.annotation.Transactional;  // 이 import로 변경
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
@Slf4j
public class RecommendService {

    private final PensionSavingsJpaRepository pensionSavingsJpaRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    @Cacheable(value = "recommendProducts", key = "'all'")
    @Transactional(readOnly = true)
    public List<RecommendProductResponseDto> recommendProduct() {
        List<RecommendProductResponseDto> recommendations = new ArrayList<>();
        
        // 각 전략별로 최적화된 쿼리 사용
        recommendations.addAll(getAggressiveRecommendations());
        recommendations.addAll(getStableRecommendations());
        recommendations.addAll(getShortTermRecommendations());
        recommendations.addAll(getLongTermRecommendations());
        recommendations.addAll(getLowCostRecommendations());
        recommendations.addAll(getStableProfitRecommendations());
        recommendations.addAll(getRiskTolerantRecommendations());

        return recommendations;
    }

    private List<RecommendProductResponseDto> getAggressiveRecommendations() {
        return pensionSavingsJpaRepository.findAggressiveProducts(5.0).stream()
                .map(product -> RecommendProductResponseDto.builder()
                        .productId(product.getProductId())
                        .strategyType(Strategy.AGGRESSIVE.getDescription())
                        .build())
                .collect(Collectors.toList());
    }

    private List<RecommendProductResponseDto> getStableRecommendations() {
        // Implementation needed
        return new ArrayList<>();
    }

    private List<RecommendProductResponseDto> getShortTermRecommendations() {
        // Implementation needed
        return new ArrayList<>();
    }

    private List<RecommendProductResponseDto> getLongTermRecommendations() {
        // Implementation needed
        return new ArrayList<>();
    }

    private List<RecommendProductResponseDto> getLowCostRecommendations() {
        // Implementation needed
        return new ArrayList<>();
    }

    private List<RecommendProductResponseDto> getStableProfitRecommendations() {
        // Implementation needed
        return new ArrayList<>();
    }

    private List<RecommendProductResponseDto> getRiskTolerantRecommendations() {
        // Implementation needed
        return new ArrayList<>();
    }

    @Cacheable(value = "productScores", key = "#product.productId + '_' + #strategy")
    public double calculateScore(PensionSavingsProductEntity product, Strategy strategy) {
        log.info("Cache miss for productScore - calculating score for product {} and strategy {}",
                product.getProductId(), strategy);

        ProductDetailEntity detail = product.getProductDetails().stream()
                .findFirst()
                .orElseThrow(() -> new FinanceHandler(ErrorStatus.PRODUCT_NOT_FOUND));

        boolean hasGuarantees = "Y".equals(detail.getGuarantees());
        double reserveRatio = calculateReserveRatio(detail);

        return switch (strategy) {
            case AGGRESSIVE -> calculateAggressiveScore(detail);
            case STABLE -> calculateStableScore(detail, hasGuarantees);
            case SHORT_TERM -> calculateShortTermScore(detail, reserveRatio);
            case LONG_TERM -> calculateLongTermScore(detail, hasGuarantees);
            case LOW_COST -> calculateLowCostScore(detail, reserveRatio);
            case STABLE_PROFIT -> calculateStableProfitScore(detail, hasGuarantees);
            case RISK_TOLERANT -> calculateRiskTolerantScore(detail, hasGuarantees);
        };
    }

    private double calculateReserveRatio(ProductDetailEntity detail) {
        return detail.getBalance() != null && detail.getBalance() != 0
                ? detail.getReserve() / (double) detail.getBalance()
                : 0.0;
    }

    private double calculateAggressiveScore(ProductDetailEntity detail) {
        return 0.6 * detail.getEarnRate() +
                0.2 * detail.getEarnRate1() +
                0.2 * detail.getAvgEarnRate10();
    }

    private double calculateStableScore(ProductDetailEntity detail, boolean hasGuarantees) {
        return (hasGuarantees ? 1.0 : 0.0) +
                0.4 * detail.getAvgEarnRate5() +
                0.4 * detail.getAvgEarnRate3() -
                0.2 * detail.getFeeRate1();
    }

    private double calculateShortTermScore(ProductDetailEntity detail, double reserveRatio) {
        return 0.5 * detail.getEarnRate() +
                0.3 * detail.getEarnRate1() +
                0.2 * reserveRatio;
    }

    private double calculateLongTermScore(ProductDetailEntity detail, boolean hasGuarantees) {
        return 0.4 * detail.getAvgEarnRate10() +
                0.4 * detail.getAvgEarnRate7() -
                0.2 * detail.getFeeRate1() +
                (hasGuarantees ? 0.5 : 0.0);
    }

    private double calculateLowCostScore(ProductDetailEntity detail, double reserveRatio) {
        return -0.5 * detail.getFeeRate1() -
                0.3 * detail.getAvgFeeRate3() +
                0.2 * reserveRatio;
    }

    private double calculateStableProfitScore(ProductDetailEntity detail, boolean hasGuarantees) {
        double volatility = Math.abs(detail.getEarnRate() - detail.getAvgEarnRate3());
        return -0.3 * volatility +
                0.4 * detail.getAvgEarnRate10() +
                (hasGuarantees ? 0.3 : 0.0);
    }

    private double calculateRiskTolerantScore(ProductDetailEntity detail, boolean hasGuarantees) {
        double volatility = Math.abs(detail.getEarnRate1() - detail.getEarnRate());
        return 0.7 * detail.getEarnRate() +
                0.3 * volatility -
                (hasGuarantees ? 0.5 : 0.0);
    }

    @CacheEvict(value = {"recommendProducts", "productScores"}, allEntries = true)
    public void refreshCache() {
        log.info("Clearing all caches");
    }

    @CacheEvict(value = "productScores", key = "#productId + '_' + #strategy")
    public void refreshProductCache(Long productId, Strategy strategy) {
        log.info("Clearing cache for product {} and strategy {}", productId, strategy);
    }

    private PensionSavingsResponseComparisonDto buildResponseDto(PensionSavingsProductEntity product) {
        ProductDetailEntity detail = product.getProductDetails().stream()
                .findFirst()
                .orElseThrow(() -> new FinanceHandler(ErrorStatus.PRODUCT_NOT_FOUND));

        DecimalFormat formatter = new DecimalFormat("#,###");

        return PensionSavingsResponseComparisonDto.builder()
                .area(product.getProductArea().getDescription())
                .company(product.getCompany())
                .product(product.getProductName())
                .productType(detail.getProductType())
                .rcvMethod(detail.getRcvMethod())
                .feeType(detail.getFeeType())
                .sells("Y".equals(detail.getSells()) ? "진행" : "중단")
                .withdraws("Y".equals(detail.getWithdraws()) ? "가능" : "불가능")
                .guarantees("Y".equals(detail.getGuarantees()) ? "보장" : "비보장")
                .currentBalance(formatter.format(detail.getBalance()))
                .previousYearBalance(formatter.format(detail.getBalance1()))
                .twoYearsAgoBalance(formatter.format(detail.getBalance2()))
                .threeYearsAgoBalance(formatter.format(detail.getBalance3()))
                .currentReserve(formatter.format(detail.getReserve()))
                .previousYearReserve(formatter.format(detail.getReserve1()))
                .twoYearsAgoReserve(formatter.format(detail.getReserve2()))
                .threeYearsAgoReserve(formatter.format(detail.getReserve3()))
                .currentEarnRate(detail.getEarnRate())
                .previousYearEarnRate(detail.getEarnRate1())
                .twoYearsAgoEarnRate(detail.getEarnRate2())
                .threeYearsAgoEarnRate(detail.getEarnRate3())
                .previousYearFeeRate(detail.getFeeRate1())
                .twoYearsAgoFeeRate(detail.getFeeRate2())
                .threeYearsAgoFeeRate(detail.getFeeRate3())
                .build();
    }
}