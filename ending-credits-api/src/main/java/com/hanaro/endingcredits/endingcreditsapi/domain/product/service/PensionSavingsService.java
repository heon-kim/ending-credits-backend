package com.hanaro.endingcredits.endingcreditsapi.domain.product.service;

import com.hanaro.endingcredits.endingcreditsapi.domain.product.dto.*;
import com.hanaro.endingcredits.endingcreditsapi.domain.product.entities.*;
import com.hanaro.endingcredits.endingcreditsapi.domain.product.repository.elasticsearch.PensionSavingsSearchRepository;
import com.hanaro.endingcredits.endingcreditsapi.domain.product.repository.jpa.PensionSavingsJpaRepository;
import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.code.status.ErrorStatus;
import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.exception.handler.FinanceHandler;
import com.hanaro.endingcredits.endingcreditsapi.utils.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
public class PensionSavingsService {
    private final ProductMapper productMapper;
    private final RestTemplate restTemplate = new RestTemplate();
    private final PensionSavingsJpaRepository pensionProductRepository;
    private final PensionSavingsSearchRepository pensionSavingsSearchRepository;

    private ProductDetailEntity createProductDetail(Map<String, Object> data, PensionSavingsProductEntity product) {
        try {
            return ProductDetailEntity.builder()
                    .product(product)
                    .earnRate(getDoubleValue(data, "earnRate"))
                    .earnRate1(getDoubleValue(data, "earnRate1"))
                    .earnRate2(getDoubleValue(data, "earnRate2"))
                    .earnRate3(getDoubleValue(data, "earnRate3"))
                    .avgEarnRate3(getDoubleValue(data, "avgEarnRate3"))
                    .avgEarnRate5(getDoubleValue(data, "avgEarnRate5"))
                    .avgEarnRate7(getDoubleValue(data, "avgEarnRate7"))
                    .avgEarnRate10(getDoubleValue(data, "avgEarnRate10"))
                    .feeRate1(getDoubleValue(data, "feeRate1"))
                    .feeRate2(getDoubleValue(data, "feeRate2"))
                    .feeRate3(getDoubleValue(data, "feeRate3"))
                    .avgFeeRate3(getDoubleValue(data, "avgFeeRate3"))
                    .balance(getIntValue(data, "balance"))
                    .balance1(getIntValue(data, "balance1"))
                    .balance2(getIntValue(data, "balance2"))
                    .balance3(getIntValue(data, "balance3"))
                    .reserve(getIntValue(data, "reserve"))
                    .reserve1(getIntValue(data, "reserve1"))
                    .reserve2(getIntValue(data, "reserve2"))
                    .reserve3(getIntValue(data, "reserve3"))
                    .productType(getStringValue(data, "productType"))
                    .rcvMethod(getStringValue(data, "rcvMethod"))
                    .feeType(getStringValue(data, "feeType"))
                    .sells(getStringValue(data, "sells"))
                    .withdraws(getStringValue(data, "withdraws"))
                    .guarantees(getStringValue(data, "guarantees"))
                    .build();
        } catch (Exception e) {
            log.error("상품 상세 정보 생성 실패: ", e);
            throw new FinanceHandler(ErrorStatus.PRODUCT_NOT_FOUND);
        }
    }

    private PensionSavingsProductEntity createProduct(Map<String, Object> data, int areaCode) {
        return PensionSavingsProductEntity.builder()
                .productName((String) data.get("product"))
                .company((String) data.get("company"))
                .productArea(ProductArea.fromCode(areaCode))
                .build();
    }

    private void saveProducts(List<PensionSavingsProductEntity> products) {
        try {
            long postgresTime = testPostgreSQLWriteSpeed(products);
            long elasticTime = testElasticsearchWriteSpeed(products);
            log.info("저장 성능 - PostgreSQL: {}ms, Elasticsearch: {}ms", postgresTime, elasticTime);
        } catch (Exception e) {
            log.error("상품 저장 실패: ", e);
            throw new FinanceHandler(ErrorStatus.DATABASE_ERROR);
        }
    }

    @Transactional
    public void fetchAndSavePensionProducts(String apiUrl, int areaCode) {
        try {
            PensionSavingsResponse response = restTemplate.getForObject(apiUrl, PensionSavingsResponse.class);
            if (response == null || response.getList() == null || response.getList().isEmpty()) {
                log.warn("외부 API에서 데이터를 가져오지 못했습니다.");
                throw new FinanceHandler(ErrorStatus.PRODUCT_NOT_FOUND);
            }

            List<PensionSavingsProductEntity> products = response.getList().stream()
                    .map(data -> {
                        try {
                            PensionSavingsProductEntity product = createProduct(data, areaCode);
                            ProductDetailEntity detail = createProductDetail(data, product);
                            product.addProductDetail(detail);
                            return product;
                        } catch (Exception e) {
                            log.error("상품 데이터 변환 실패: ", e);
                            throw new FinanceHandler(ErrorStatus.PRODUCT_NOT_FOUND);
                        }
                    })
                    .collect(Collectors.toList());

            saveProducts(products);
        } catch (FinanceHandler e) {
            throw e;  // 이미 FinanceHandler인 경우 그대로 전파
        } catch (Exception e) {
            log.error("연금저축 상품 데이터 가져오기 실패: ", e);
            throw new FinanceHandler(ErrorStatus.PRODUCT_NOT_FOUND);
        }
    }

    /**
     *  PostgreSQL vs Elasticsearch 쓰기 성능 비교
     */
    private void compareWritePerformance(List<Map<String, Object>> productList, int areaCode) {
        List<PensionSavingsProductEntity> products = productList.stream()
                .map(data -> {
                    PensionSavingsProductEntity product = PensionSavingsProductEntity.builder()
                        .productName((String) data.get("product"))
                        .company((String) data.get("company"))
                        .productArea(ProductArea.fromCode(areaCode))
                        .build();

                    ProductDetailEntity detail = ProductDetailEntity.builder()
                            .product(product)
                            .earnRate(getDoubleValue(data, "earnRate"))
                            .earnRate1(getDoubleValue(data, "earnRate1"))
                            .earnRate2(getDoubleValue(data, "earnRate2"))
                            .earnRate3(getDoubleValue(data, "earnRate3"))
                            .avgEarnRate3(getDoubleValue(data, "avgEarnRate3"))
                            .avgEarnRate5(getDoubleValue(data, "avgEarnRate5"))
                            .avgEarnRate7(getDoubleValue(data, "avgEarnRate7"))
                            .avgEarnRate10(getDoubleValue(data, "avgEarnRate10"))
                            .feeRate1(getDoubleValue(data, "feeRate1"))
                            .feeRate2(getDoubleValue(data, "feeRate2"))
                            .feeRate3(getDoubleValue(data, "feeRate3"))
                            .avgFeeRate3(getDoubleValue(data, "avgFeeRate3"))
                            .balance(getIntValue(data, "balance"))
                            .balance1(getIntValue(data, "balance1"))
                            .balance2(getIntValue(data, "balance2"))
                            .balance3(getIntValue(data, "balance3"))
                            .reserve(getIntValue(data, "reserve"))
                            .reserve1(getIntValue(data, "reserve1"))
                            .reserve2(getIntValue(data, "reserve2"))
                            .reserve3(getIntValue(data, "reserve3"))
                            .productType((String) data.get("productType"))
                            .rcvMethod((String) data.get("rcvMethod"))
                            .feeType((String) data.get("feeType"))
                            .sells((String) data.get("sells"))
                            .withdraws((String) data.get("withdraws"))
                            .guarantees((String) data.get("guarantees"))
                            .build();

                    product.getProductDetails().add(detail);
                    return product;
                })
                .collect(Collectors.toList());


        long postgresTime = testPostgreSQLWriteSpeed(products);
        long elasticTime = testElasticsearchWriteSpeed(products);

        log.info(" PostgreSQL 삽입 성능: {}ms", postgresTime);
        log.info(" Elasticsearch 삽입 성능: {}ms", elasticTime);
    }

    /**
     *  PostgreSQL 대량 삽입 성능 측정
     */
    @Transactional
    public long testPostgreSQLWriteSpeed(List<PensionSavingsProductEntity> products) {
        long startTime = System.currentTimeMillis();
        pensionProductRepository.saveAll(products);
        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }

    /**
     *  Elasticsearch 대량 삽입 성능 측정
     */
    @Transactional
    public long testElasticsearchWriteSpeed(List<PensionSavingsProductEntity> products) {
        List<PensionSavingsSearchItems> documents = products.stream()
                .map(productMapper::toPensionSavingsSearchItems)
                .collect(Collectors.toList());

        long startTime = System.currentTimeMillis();
        pensionSavingsSearchRepository.saveAll(documents);
        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }

    @Transactional(readOnly = true)
    public SliceResponse<PensionSavingsListResponseDto> getAllSavingsProductList(Pageable pageable) {
        Slice<PensionSavingsProductEntity> pensionSavingsProducts = pensionProductRepository.findAllBy(pageable);

        Slice<PensionSavingsListResponseDto> mappedProducts = pensionSavingsProducts.map(product ->
                PensionSavingsListResponseDto.builder()
                        .productId(product.getProductId())
                        .productName(product.getProductName())
                        .company(product.getCompany())
                        .build()
        );

        return new SliceResponse<>(mappedProducts);
    }

    @Transactional(readOnly = true)
    public List<PensionSavingsListResponseDto> getSavingsProductListByAreaCode(int areaCode) {
        ProductArea productArea = ProductArea.fromCode(areaCode);

        return pensionProductRepository.findByProductArea(productArea)
                .stream()
                .map(product -> PensionSavingsListResponseDto.builder()
                        .productId(product.getProductId())
                        .productName(product.getProductName())
                        .company(product.getCompany())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PensionSavingsResponseComparisonDto getSavingsProduct(UUID productId) {
        PensionSavingsProductEntity product = findProductById(productId);

        ProductDetailEntity detail = product.getProductDetails().stream()
                .findFirst()
                .orElseThrow(() -> new FinanceHandler(ErrorStatus.PRODUCT_NOT_FOUND));

        DecimalFormat formatter = new DecimalFormat("#,###");

        return PensionSavingsResponseComparisonDto.builder()
                .productId(productId)
                .area(product.getProductArea().getDescription())
                .company(product.getCompany())
                .product(product.getProductName())
                .productType(detail.getProductType())
                .rcvMethod(detail.getRcvMethod())
                .feeType(detail.getFeeType())
                .sells(detail.getSells().equals("Y") ? "진행" : "중단")
                .withdraws(detail.getWithdraws().equals("Y") ? "가능" : "불가능")
                .guarantees(detail.getGuarantees().equals("Y") ? "보장" : "비보장")
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

    @Transactional(readOnly = true)
    public List<PensionSavingsSearchItems> searchProducts(String keyword, int areaCode) {
        String productArea = ProductArea.fromCode(areaCode).getDescription();

        return pensionSavingsSearchRepository.findByProductNameContainingAndProductArea(keyword, productArea);
    }

    @Transactional(readOnly = true)
    public PensionSavingsDetailResponseDto getSavingsProductDetail(UUID productId) {
        PensionSavingsProductEntity product = findProductById(productId);

        ProductDetailEntity detail = product.getProductDetails().stream()
                .findFirst()
                .orElseThrow(() -> new FinanceHandler(ErrorStatus.PRODUCT_NOT_FOUND));


        return PensionSavingsDetailResponseDto.builder()
                .productArea(product.getProductArea().getDescription())
                .company(product.getCompany())
                .productName(product.getProductName())
                .productType(detail.getProductType())
                .withdraws(detail.getWithdraws().equals("Y") ? "가능" : "불가능")
                .currentEarnRate(detail.getEarnRate())
                .previousYearEarnRate(detail.getEarnRate1())
                .twoYearsAgoEarnRate(detail.getEarnRate2())
                .threeYearsAgoEarnRate(detail.getEarnRate3())
                .previousYearFeeRate(detail.getFeeRate1())
                .twoYearsAgoFeeRate(detail.getFeeRate2())
                .threeYearsAgoFeeRate(detail.getFeeRate3())
                .build();
    }

    @Transactional(readOnly = true)
    public List<PensionSavingsListResponseDto> getHanaSavingsProductList(String company) {

        return pensionProductRepository.findByCompany(company)
                .stream()
                .map(product -> PensionSavingsListResponseDto.builder()
                        .productId(product.getProductId())
                        .productName(product.getProductName())
                        .company(product.getCompany())
                        .build())
                .collect(Collectors.toList());
    }
    private PensionSavingsProductEntity findProductById(UUID productId) {
        return pensionProductRepository.findByIdWithDetails(productId)
                .orElseThrow(() -> new FinanceHandler(ErrorStatus.PRODUCT_NOT_FOUND));
    }

    private ProductDetailEntity getProductDetail(PensionSavingsProductEntity product) {
        return product.getProductDetails().stream()
                .findFirst()
                .orElseThrow(() -> new FinanceHandler(ErrorStatus.PRODUCT_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public PensionSavingsEstimateDto calculateProfit(UUID productId) {
        PensionSavingsProductEntity product = findProductById(productId);
        ProductDetailEntity detail = getProductDetail(product);

        return calculateEstimate(detail.getEarnRate());
    }

    private PensionSavingsEstimateDto calculateEstimate(Double earnRate) {
        double rate = earnRate / 100.0;
        int initialAmount = 20_000_000;
        int years = 3;

        double totalAmount = calculateTotalAmount(initialAmount, rate, years);
        int estimatedProfit = calculateEstimatedProfit(totalAmount, initialAmount);

        return PensionSavingsEstimateDto.builder()
                .expectedProfit(estimatedProfit)
                .annualAdditionalUsage(calculateAnnualUsage(estimatedProfit))
                .monthlyAdditionalUsage(calculateMonthlyUsage(estimatedProfit))
                .expectedEarnRate(rate * 100)
                .build();
    }

    private double calculateTotalAmount(int initialAmount, double rate, int years) {
        return initialAmount * Math.pow(1 + rate, years);
    }

    private int calculateEstimatedProfit(double totalAmount, int initialAmount) {
        return (int) Math.round(totalAmount - initialAmount);
    }

    private int calculateAnnualUsage(int estimatedProfit) {
        return (int) Math.round(estimatedProfit * 0.1);
    }

    private int calculateMonthlyUsage(int estimatedProfit) {
        return calculateAnnualUsage(estimatedProfit) / 12;
    }

    private String getStringValue(Map<String, Object> map, String key) {
        return Optional.ofNullable(map.get(key))
                .map(Object::toString)
                .orElse("N");  // 기본값 설정
    }

    private Integer getIntValue(Map<String, Object> map, String key) {
        return Optional.ofNullable(map.get(key))
                .filter(value -> value instanceof Number)
                .map(value -> ((Number) value).intValue())
                .orElse(0);  // 기본값 설정
    }

    private Double getDoubleValue(Map<String, Object> map, String key) {
        return Optional.ofNullable(map.get(key))
                .filter(value -> value instanceof Number)
                .map(value -> ((Number) value).doubleValue())
                .orElse(0.0);  // 기본값 설정
    }
}