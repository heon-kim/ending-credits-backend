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

    @Transactional
    public void fetchAndSavePensionProducts(String apiUrl, int areaCode) {
        PensionSavingsResponse response = restTemplate.getForObject(apiUrl, PensionSavingsResponse.class);
        if (response == null) return;

        List<Map<String, Object>> productList = response.getList();
        if (productList == null || productList.isEmpty()) {
            log.warn("외부 API에서 데이터를 가져오지 못했습니다.");
            return;
        }

        compareWritePerformance(productList, areaCode);
    }

    /**
     *  PostgreSQL vs Elasticsearch 쓰기 성능 비교
     */
    private void compareWritePerformance(List<Map<String, Object>> productList, int areaCode) {
        List<PensionSavingsProductEntity> products = productList.stream()
                .map(data -> PensionSavingsProductEntity.builder()
                        .productName((String) data.get("product"))
                        .company((String) data.get("company"))
                        .productArea(ProductArea.fromCode(areaCode))
                        .productDetail(List.of(data))
                        .build())
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
        PensionSavingsProductEntity product = pensionProductRepository.findById(productId)
                .orElseThrow(() -> new FinanceHandler(ErrorStatus.PRODUCT_NOT_FOUND));

        List<Map<String, Object>> productDetail = product.getProductDetail();
        if (productDetail == null || productDetail.isEmpty()) {
            log.warn("조회할 연금저축 상품이 존재하지 않습니다.");
        }

        Map<String, Object> detail = productDetail.get(0);

        DecimalFormat formatter = new DecimalFormat("#,###");

        return PensionSavingsResponseComparisonDto.builder()
                .productId(productId) // 상품 ID
                .area(product.getProductArea().getDescription())  // 권역
                .company((String) detail.get("company"))  // 은행명
                .product(product.getProductName())  // 상품명
                .productType((String) detail.get("productType"))  // 상품 유형
                .rcvMethod((String) detail.get("rcvMethod"))  // 수령 기간
                .feeType((String) detail.get("feeType"))  // 수수료 구조
                .sells(((String) detail.get("sells")).equals("Y") ? "진행" : "중단")  // 판매 여부
                .withdraws(((String) detail.get("withdraws")).equals("Y") ? "가능" : "불가능")  // 중도 해지
                .guarantees(((String) detail.get("guarantees")).equals("Y") ? "보장" : "비보장")  // 원금 보장
                .currentBalance(formatter.format(detail.get("balance")))  // 현재 납입원금
                .previousYearBalance(formatter.format(detail.get("balance1")))  // 과거 1년 납입원금
                .twoYearsAgoBalance(formatter.format(detail.get("balance2")))  // 과거 2년 납입원금
                .threeYearsAgoBalance(formatter.format(detail.get("balance3")))  // 과거 3년 납입원금
                .currentReserve(formatter.format(detail.get("reserve")))  // 현재 적립금
                .previousYearReserve(formatter.format(detail.get("reserve1")))  // 과거 1년 적립금
                .twoYearsAgoReserve(formatter.format(detail.get("reserve2")))  // 과거 2년 적립금
                .threeYearsAgoReserve(formatter.format(detail.get("reserve3")))  // 과거 3년 적립금
                .currentEarnRate((Double) detail.get("earnRate"))  // 현재 수익률
                .previousYearEarnRate((Double) detail.get("earnRate1"))  // 과거 1년 수익률
                .twoYearsAgoEarnRate((Double) detail.get("earnRate2"))  // 과거 2년 수익률
                .threeYearsAgoEarnRate((Double) detail.get("earnRate3"))  // 과거 3년 수익률
                .previousYearFeeRate((Double) detail.get("feeRate1"))  // 과거 1년 수수료율
                .twoYearsAgoFeeRate((Double) detail.get("feeRate2"))  // 과거 2년 수수료율
                .threeYearsAgoFeeRate((Double) detail.get("feeRate3"))  // 과거 3년 수수료율
                .build();
    }

    @Transactional(readOnly = true)
    public List<PensionSavingsSearchItems> searchProducts(String keyword, int areaCode) {
        String productArea = ProductArea.fromCode(areaCode).getDescription();

        return pensionSavingsSearchRepository.findByProductNameContainingAndProductArea(keyword, productArea);
    }

    @Transactional(readOnly = true)
    public PensionSavingsDetailResponseDto getSavingsProductDetail(UUID productId) {
        PensionSavingsProductEntity product = pensionProductRepository.findById(productId)
                .orElseThrow(() -> new FinanceHandler(ErrorStatus.PRODUCT_NOT_FOUND));

        List<Map<String, Object>> productDetail = product.getProductDetail();
        if (productDetail == null || productDetail.isEmpty()) {
            log.warn("조회할 연금저축 상품이 존재하지 않습니다.");
        }

        Map<String, Object> detail = productDetail.get(0);

        return PensionSavingsDetailResponseDto.builder()
                .productArea(product.getProductArea().getDescription())  // 권역
                .company((String) detail.get("company"))  // 기업명
                .productName(product.getProductName())  // 상품명
                .productType((String) detail.get("productType"))  // 상품 유형
                .withdraws(((String) detail.get("withdraws")).equals("Y") ? "가능" : "불가능")  // 중도 해지
                .currentEarnRate((Double) detail.get("earnRate"))  // 현재 수익률
                .previousYearEarnRate((Double) detail.get("earnRate1"))  // 과거 1년 수익률
                .twoYearsAgoEarnRate((Double) detail.get("earnRate2"))  // 과거 2년 수익률
                .threeYearsAgoEarnRate((Double) detail.get("earnRate3"))  // 과거 3년 수익률
                .previousYearFeeRate((Double) detail.get("feeRate1"))  // 과거 1년 수수료율
                .twoYearsAgoFeeRate((Double) detail.get("feeRate2"))  // 과거 2년 수수료율
                .threeYearsAgoFeeRate((Double) detail.get("feeRate3"))  // 과거 3년 수수료율
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


    @Transactional(readOnly = true)
    public PensionSavingsEstimateDto calculateProfit(UUID productId) {
        PensionSavingsProductEntity product = pensionProductRepository.findById(productId)
                .orElseThrow(() -> new FinanceHandler(ErrorStatus.PRODUCT_NOT_FOUND));

        double currentEarnRate = (double) product.getProductDetail().get(0).get("earnRate") / 100.0;

        int initialAmount = 20000000;  // 초기 금액 2천만 원

        double totalAmountAfter3Years = initialAmount * Math.pow(1 + currentEarnRate, 3);

        // 예상 수익액 = 3년 후 총 금액 - 초기 금액
        int estimatedProfit = (int) Math.round(totalAmountAfter3Years - initialAmount);

        // 연간 추가 사용 금액 = 예상 수익액의 10%으로 설정
        int annualUsagePercentage = 10;
        int annualAdditionalUsage = (int) Math.round(estimatedProfit * annualUsagePercentage / 100.0);

        // 월간 추가 사용 금액
        int monthlyAdditionalUsage = annualAdditionalUsage / 12;

        return PensionSavingsEstimateDto.builder()
                .expectedProfit(estimatedProfit)
                .annualAdditionalUsage(annualAdditionalUsage)
                .monthlyAdditionalUsage(monthlyAdditionalUsage)
                .expectedEarnRate(currentEarnRate * 100)
                .build();
    }
}