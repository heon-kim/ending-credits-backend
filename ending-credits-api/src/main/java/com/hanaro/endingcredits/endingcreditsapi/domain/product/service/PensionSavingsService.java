package com.hanaro.endingcredits.endingcreditsapi.domain.product.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hanaro.endingcredits.endingcreditsapi.domain.product.dto.PensionSavingsListResponseDto;
import com.hanaro.endingcredits.endingcreditsapi.domain.product.dto.PensionSavingsResponseDto;
import com.hanaro.endingcredits.endingcreditsapi.domain.product.dto.PensionSavingsResponse;
import com.hanaro.endingcredits.endingcreditsapi.domain.product.entities.*;
import com.hanaro.endingcredits.endingcreditsapi.domain.product.repository.elasticsearch.PensionSavingsSearchRepository;
import com.hanaro.endingcredits.endingcreditsapi.domain.product.repository.jpa.PensionSavingsJpaRepository;
import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.code.status.ErrorStatus;
import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.exception.handler.ProductHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PensionSavingsService {

    private final RestTemplate restTemplate;

    private final PensionSavingsJpaRepository pensionProductRepository;
    private final PensionSavingsSearchRepository pensionSavingsSearchRepository;

    @Transactional
    public void fetchAndSavePensionProducts(String apiUrl, int areaCode) throws JsonProcessingException {
        PensionSavingsResponse response = restTemplate.getForObject(apiUrl, PensionSavingsResponse.class);
        if (response == null) return;

        List<Map<String, Object>> limitedList = response.getList()
                .stream()
                .limit(100)  // 리스트에서 처음 100개만 가져옴
                .collect(Collectors.toList());

        if (response.getList() != null && !response.getList().isEmpty()) {
            for (Map<String, Object> productData : response.getList()) {
                String productName = (String) productData.get("product");
                String company = (String) productData.get("company");

                if (productName == null || company == null) {
                    log.warn("product 또는 company가 존재하지 않습니다." + productData);
                    continue;
                }

                List<Map<String, Object>> productDetail = new ArrayList<>();
                productDetail.add(productData);
                ProductArea productArea = ProductArea.fromCode(areaCode);

                PensionSavingsProductEntity entity = PensionSavingsProductEntity.builder()
                        .productName(productName)
                        .productArea(productArea)
                        .company(company)
                        .productDetail(productDetail)
                        .build();
                pensionProductRepository.save(entity);
                PensionSavingsEsEntity document = mapToEsEntity(entity);
                pensionSavingsSearchRepository.save(document);
            }
        }
    }

    @Transactional(readOnly = true)
    public List<PensionSavingsListResponseDto> getAllSavingsProductList() {

        return pensionProductRepository.findAll()
                .stream()
                .map(product -> PensionSavingsListResponseDto.builder()
                        .productId(product.getProductId())
                        .productName(product.getProductName())
                        .company(product.getCompany())
                        .build())
                .collect(Collectors.toList());
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
    public PensionSavingsResponseDto getSavingsProduct(UUID productId) {
        PensionSavingsProductEntity product = pensionProductRepository.findById(productId)
                .orElseThrow(() -> new ProductHandler(ErrorStatus.PRODUCT_NOT_FOUND));

        List<Map<String, Object>> productDetail = product.getProductDetail();
        if (productDetail == null || productDetail.isEmpty()) {
            log.warn("조회할 연금저축 상품이 존재하지 않습니다.");
        }

        Map<String, Object> detail = productDetail.get(0);

        return PensionSavingsResponseDto.builder()
                .area(product.getProductArea().getDescription())  // 권역
                .company((String) detail.get("company"))  // 은행명
                .product(product.getProductName())  // 상품명
                .productType((String) detail.get("productType"))  // 상품 유형
                .rcvMethod((String) detail.get("rcvMethod"))  // 수령 기간
                .feeType((String) detail.get("feeType"))  // 수수료 구조
                .sells(((String) detail.get("sells")).equals("Y") ? "진행" : "중단")  // 판매 여부
                .withdraws(((String) detail.get("withdraws")).equals("Y") ? "가능" : "불가능")  // 중도 해지
                .guarantees(((String) detail.get("guarantees")).equals("Y") ? "보장" : "비보장")  // 원금 보장
                .currentBalance((int) detail.get("balance"))  // 현재 납입원금
                .previousYearBalance((int) detail.get("balance1"))  // 과거 1년 납입원금
                .twoYearsAgoBalance((int) detail.get("balance2"))  // 과거 2년 납입원금
                .threeYearsAgoBalance((int) detail.get("balance3"))  // 과거 3년 납입원금
                .currentReserve((int) detail.get("reserve"))  // 현재 적립금
                .previousYearReserve((int) detail.get("reserve1"))  // 과거 1년 적립금
                .twoYearsAgoReserve((int) detail.get("reserve2"))  // 과거 2년 적립금
                .threeYearsAgoReserve((int) detail.get("reserve3"))  // 과거 3년 적립금
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
    public List<PensionSavingsEsEntity> searchProducts(String keyword, int areaCode) {
        String productArea = ProductArea.fromCode(areaCode).getDescription();

        return pensionSavingsSearchRepository.findByProductNameContainingAndProductArea(keyword, productArea);
    }

    private PensionSavingsEsEntity mapToEsEntity(PensionSavingsProductEntity entity) {
        return PensionSavingsEsEntity.builder()
                .productId((entity.getProductId()).toString())
                .productName(entity.getProductName())
                .productArea(entity.getProductArea().getDescription())
                .company(entity.getCompany())
                .build();
    }
}