package com.hanaro.endingcredits.endingcreditsapi.domain.product.service;

import com.hanaro.endingcredits.endingcreditsapi.domain.product.dto.PensionSavingsResponseDto;
import com.hanaro.endingcredits.endingcreditsapi.domain.product.entities.PensionSavingsProductEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.product.entities.Strategy;
import com.hanaro.endingcredits.endingcreditsapi.domain.product.repository.jpa.PensionSavingsJpaRepository;
import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.code.status.ErrorStatus;
import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.exception.handler.ProductHandler;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RecommendService {

    private final PensionSavingsJpaRepository pensionSavingsJpaRepository;

    // 가장 높은 점수를 가진 상품을 반환
    @Transactional
    public PensionSavingsResponseDto recommendProduct(Strategy strategy) {
        List<PensionSavingsProductEntity> products = pensionSavingsJpaRepository.findAll();

        PensionSavingsProductEntity bestProduct = products.stream()
                .max((p1, p2) -> Double.compare(calculateScore(p1, strategy), calculateScore(p2, strategy)))
                .orElseThrow(() -> new ProductHandler(ErrorStatus.PRODUCT_NOT_FOUND));

        return buildResponseDto(bestProduct);
    }

    private double getDoubleValue(Object value) {
        if (value instanceof Integer) {
            return ((Integer) value).doubleValue();
        } else if (value instanceof Double) {
            return (Double) value;
        }
        return 0.0; // 기본값
    }

    private int getIntValue(Object value) {
        if (value instanceof Integer) {
            return (Integer) value;
        } else if (value instanceof Double) {
            return ((Double) value).intValue();
        }
        return 0; // 기본값
    }


    private double calculateScore(PensionSavingsProductEntity product, Strategy strategy) {
        double score = 0.0;

        Map<String, Object> detail = product.getProductDetail().get(0);

        switch (strategy) {
            case AGGRESSIVE:
                score += 0.6 * getDoubleValue(detail.get("earnRate"));
                score += 0.2 * getDoubleValue(detail.get("earnRate1"));
                score += 0.2 * getDoubleValue(detail.get("avgEarnRate10"));
                break;
            case STABLE:
                score += "Y".equals(detail.getOrDefault("guarantees", "N")) ? 1.0 : 0.0;
                score += 0.4 * getDoubleValue(detail.get("avgEarnRate5"));
                score += 0.4 * getDoubleValue(detail.get("avgEarnRate3"));
                score -= 0.2 * getDoubleValue(detail.get("feeRate1"));
                break;
            case SHORT_TERM:
                score += 0.5 * getDoubleValue(detail.get("earnRate"));
                score += 0.3 * getDoubleValue(detail.get("earnRate1"));
                score += 0.2 * (getIntValue(detail.get("reserve")) / (double) getIntValue(detail.get("balance")));
                break;
            case LONG_TERM:
                score += 0.4 * getDoubleValue(detail.get("avgEarnRate10"));
                score += 0.4 * getDoubleValue(detail.get("avgEarnRate7"));
                score -= 0.2 * getDoubleValue(detail.get("feeRate1"));
                score += "Y".equals(detail.getOrDefault("guarantees", "N")) ? 0.5 : 0.0;
                break;
            case LOW_COST:
                score -= 0.5 * getDoubleValue(detail.get("feeRate1"));
                score -= 0.3 * getDoubleValue(detail.get("avgFeeRate3"));
                score += 0.2 * (getIntValue(detail.get("reserve")) / (double) getIntValue(detail.get("balance")));
                break;
            case STABLE_PROFIT:
                double volatility = Math.abs(getDoubleValue(detail.get("earnRate")) - getDoubleValue(detail.get("avgEarnRate3")));
                score -= 0.3 * volatility;
                score += 0.4 * getDoubleValue(detail.get("avgEarnRate10"));
                score += "Y".equals(detail.getOrDefault("guarantees", "N")) ? 0.3 : 0.0;
                break;
            case RISK_TOLERANT:
                score += 0.7 * getDoubleValue(detail.get("earnRate"));
                score += 0.3 * Math.abs(getDoubleValue(detail.get("earnRate1")) - getDoubleValue(detail.get("earnRate")));
                score -= "Y".equals(detail.getOrDefault("guarantees", "N")) ? 0.5 : 0.0;
                break;
        }

        return score;
    }

    // 상품 상세 정보를 Dto로 변환
    private PensionSavingsResponseDto buildResponseDto(PensionSavingsProductEntity product) {
        Map<String, Object> detail = product.getProductDetail().get(0);

        DecimalFormat formatter = new DecimalFormat("#,###");

        return PensionSavingsResponseDto.builder()
                .area(product.getProductArea().getDescription())
                .company((String) detail.get("company"))
                .product(product.getProductName())
                .productType((String) detail.get("productType"))
                .rcvMethod((String) detail.get("rcvMethod"))
                .feeType((String) detail.get("feeType"))
                .sells("Y".equals(detail.get("sells")) ? "진행" : "중단")
                .withdraws("Y".equals(detail.get("withdraws")) ? "가능" : "불가능")
                .guarantees("Y".equals(detail.get("guarantees")) ? "보장" : "비보장")
                .currentBalance(formatter.format(getIntValue(detail.get("balance"))))
                .previousYearBalance(formatter.format(getIntValue(detail.get("balance1"))))
                .twoYearsAgoBalance(formatter.format(getIntValue(detail.get("balance2"))))
                .threeYearsAgoBalance(formatter.format(getIntValue(detail.get("balance3"))))
                .currentReserve(formatter.format(getIntValue(detail.get("reserve"))))
                .previousYearReserve(formatter.format(getIntValue(detail.get("reserve1"))))
                .twoYearsAgoReserve(formatter.format(getIntValue(detail.get("reserve2"))))
                .threeYearsAgoReserve(formatter.format(getIntValue(detail.get("reserve3"))))
                .currentEarnRate(getDoubleValue(detail.get("earnRate")))
                .previousYearEarnRate(getDoubleValue(detail.get("earnRate1")))
                .twoYearsAgoEarnRate(getDoubleValue(detail.get("earnRate2")))
                .threeYearsAgoEarnRate(getDoubleValue(detail.get("earnRate3")))
                .previousYearFeeRate(getDoubleValue(detail.get("feeRate1")))
                .twoYearsAgoFeeRate(getDoubleValue(detail.get("feeRate2")))
                .threeYearsAgoFeeRate(getDoubleValue(detail.get("feeRate3")))
                .build();
    }

}
