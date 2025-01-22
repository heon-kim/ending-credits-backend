package com.hanaro.endingcredits.endingcreditsapi.domain.product.service;

import com.hanaro.endingcredits.endingcreditsapi.domain.product.dto.*;
import com.hanaro.endingcredits.endingcreditsapi.domain.product.entities.*;
import com.hanaro.endingcredits.endingcreditsapi.domain.product.repository.elasticsearch.RetirementPensionEsRepository;
import com.hanaro.endingcredits.endingcreditsapi.domain.product.repository.jpa.RetirementPensionJpaRepository;
import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.code.status.ErrorStatus;
import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.exception.handler.ProductHandler;
import com.hanaro.endingcredits.endingcreditsapi.utils.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class RetirementPensionService {
    private final ProductMapper productMapper;
    private final RestTemplate restTemplate;
    private final RetirementPensionJpaRepository retirementPensionJpaRepository;
    private final RetirementPensionEsRepository retirementPensionEsRepository;

    @Transactional
    public void fetchAndSaveAnnuityProducts(String apiUrl) {
        RetirementPensionResponse response = restTemplate.getForObject(apiUrl, RetirementPensionResponse.class);
        if (response == null) return;

        if (response.getList() != null && !response.getList().isEmpty()) {
            for (CompanyDto companyList: response.getList()) {
                Optional<RetirementPensionCompanyEntity> existingEntity = retirementPensionJpaRepository.findByCompany(companyList.getCompany());

                if (existingEntity.isPresent()) {
                    // 기존 엔티티 업데이트
                    RetirementPensionCompanyEntity entity = existingEntity.get();
                    List<Map<String, Object>> yieldDetail = entity.getYieldDetail();
                    yieldDetail.add((companyList.getList()).get(0));
                    retirementPensionJpaRepository.save(entity);
                } else {
                        String company = companyList.getCompany();
                        String area = companyList.getArea();
                        List<Map<String, Object>> yieldDetail = companyList.getList();

                        if (company == null || area == null || yieldDetail == null || yieldDetail.isEmpty()) {
                            log.warn("company 또는 area 또는 yieldDetail가 존재하지 않습니다.");
                            continue;
                        }

                        ProductArea yieldArea = ProductArea.fromDescription(area);

                        RetirementPensionCompanyEntity entity = RetirementPensionCompanyEntity.builder()
                                .company(company)
                                .area(yieldArea)
                                .yieldDetail(yieldDetail)
                                .build();
                        retirementPensionJpaRepository.save(entity);
//                PensionSavingsEsEntity document = productMapper.toPensionSavingsEsEntity(entity);
//                retirementPensionEsRepository.save(document);
                }
            }
        }
    }


//    @Transactional
//    public void saveOrUpdate(RetirementPensionProductDto dto, ProductArea productArea, SysType sysType) {
//        Optional<RetirementPensionYieldEntity> existingEntity =
//                retirementPensionJpaRepository.findByProductNameAndCompany(dto.getProduct(), dto.getCompany());
//
//        if (existingEntity.isPresent()) {
//            RetirementPensionYieldEntity entity = existingEntity.get();
//            entity.update(productMapper.toRetirementPensionProductEntity(dto, productArea, sysType));
//            retirementPensionJpaRepository.save(entity);
//            retirementPensionEsRepository.save(productMapper.toRetirementPensionEsEntity(entity));
//        } else {
//            RetirementPensionYieldEntity newEntity = productMapper.toRetirementPensionProductEntity(dto, productArea, sysType);
//            retirementPensionJpaRepository.save(newEntity);
//            retirementPensionEsRepository.save(productMapper.toRetirementPensionEsEntity(newEntity));
//        }
//    }

//    @Transactional(readOnly = true)
//    public List<RetirementPensionEsEntity> searchProducts(String keyword, int areaCode, int sysTypeCode) {
//        ProductArea productArea = ProductArea.fromCode(areaCode);
//        SysType sysType = SysType.fromCode(sysTypeCode);
//
//        return retirementPensionEsRepository.findByProductNameContainingAndProductAreaAndSysType(keyword, productArea, sysType);
//    }

//    @Transactional(readOnly = true)
//    public List<RetirementPensionProductSummaryDto> getPensionProducts(int areaCode, int sysTypeCode) {
//        ProductArea productArea = ProductArea.fromCode(areaCode);
//        SysType sysType = SysType.fromCode(sysTypeCode);
//
//        return retirementPensionJpaRepository.findByProductAreaAndSysType(productArea, sysType)
//                .stream()
//                .map(product -> new RetirementPensionProductSummaryDto(product.getProductId(), product.getProductName()))
//                .collect(Collectors.toList());
//    }

    @Transactional(readOnly = true)
    public RetirementPensionDetailResponseDto getPensionProductDetailById(UUID companyId) {
        RetirementPensionCompanyEntity yieldEntity = retirementPensionJpaRepository.findById(companyId)
                .orElseThrow(() -> new ProductHandler(ErrorStatus.PRODUCT_NOT_FOUND));

        List<Map<String, Object>> yieldDetail = yieldEntity.getYieldDetail();


        for (Map<String, Object> yield : yieldDetail) {

        }

        YieldDetailDto guaranteedYieldDetails = YieldDetailDto.builder()
                .dbEarnRate(getDoubleValue(yieldDetail.get(0), "dbEarnRate", 0.0))
                .dbEarnRate3(getDoubleValue(yieldDetail.get(0), "dbEarnRate3", 0.0))
                .dbEarnRate5(getDoubleValue(yieldDetail.get(0), "dbEarnRate5", 0.0))
                .dbEarnRate7(getDoubleValue(yieldDetail.get(0), "dbEarnRate7", 0.0))
                .dbEarnRate10(getDoubleValue(yieldDetail.get(0), "dbEarnRate10", 0.0))
                .dcEarnRate(getDoubleValue(yieldDetail.get(0), "dcEarnRate", 0.0))
                .dcEarnRate3(getDoubleValue(yieldDetail.get(0), "dcEarnRate3", 0.0))
                .dcEarnRate5(getDoubleValue(yieldDetail.get(0), "dcEarnRate5", 0.0))
                .dcEarnRate7(getDoubleValue(yieldDetail.get(0), "dcEarnRate7", 0.0))
                .dcEarnRate10(getDoubleValue(yieldDetail.get(0), "dcEarnRate10", 0.0))
                .irpEarnRate(getDoubleValue(yieldDetail.get(0), "irpEarnRate", 0.0))
                .irpEarnRate3(getDoubleValue(yieldDetail.get(0), "irpEarnRate3", 0.0))
                .irpEarnRate5(getDoubleValue(yieldDetail.get(0), "irpEarnRate5", 0.0))
                .irpEarnRate7(getDoubleValue(yieldDetail.get(0), "irpEarnRate7", 0.0))
                .irpEarnRate10(getDoubleValue(yieldDetail.get(0), "irpEarnRate10", 0.0))
                .build();

        YieldDetailDto nonGuaranteedYieldDetails = YieldDetailDto.builder()
                .dbEarnRate(getDoubleValue(yieldDetail.get(1), "dbEarnRate", 0.0))
                .dbEarnRate3(getDoubleValue(yieldDetail.get(1), "dbEarnRate3", 0.0))
                .dbEarnRate5(getDoubleValue(yieldDetail.get(1), "dbEarnRate5", 0.0))
                .dbEarnRate7(getDoubleValue(yieldDetail.get(1), "dbEarnRate7", 0.0))
                .dbEarnRate10(getDoubleValue(yieldDetail.get(1), "dbEarnRate10", 0.0))
                .dcEarnRate(getDoubleValue(yieldDetail.get(1), "dcEarnRate", 0.0))
                .dcEarnRate3(getDoubleValue(yieldDetail.get(1), "dcEarnRate3", 0.0))
                .dcEarnRate5(getDoubleValue(yieldDetail.get(1), "dcEarnRate5", 0.0))
                .dcEarnRate7(getDoubleValue(yieldDetail.get(1), "dcEarnRate7", 0.0))
                .dcEarnRate10(getDoubleValue(yieldDetail.get(1), "dcEarnRate10", 0.0))
                .irpEarnRate(getDoubleValue(yieldDetail.get(1), "irpEarnRate", 0.0))
                .irpEarnRate3(getDoubleValue(yieldDetail.get(1), "irpEarnRate3", 0.0))
                .irpEarnRate5(getDoubleValue(yieldDetail.get(1), "irpEarnRate5", 0.0))
                .irpEarnRate7(getDoubleValue(yieldDetail.get(1), "irpEarnRate7", 0.0))
                .irpEarnRate10(getDoubleValue(yieldDetail.get(1), "irpEarnRate10", 0.0))
                .build();


        Map<String, YieldDetailDto> earnRates = Map.of(
                "원리금 보장", guaranteedYieldDetails,
                "원리금 비보장", nonGuaranteedYieldDetails
        );

        return RetirementPensionDetailResponseDto.builder()
                .company(yieldEntity.getCompany())
                .area(yieldEntity.getArea().getDescription())
                .earnRates(earnRates)
                .build();
    }

    private double getDoubleValue(Map<String, Object> map, String key, double defaultValue) {
        return Optional.ofNullable(map.get(key))
                .filter(value -> value instanceof Number)
                .map(value -> ((Number) value).doubleValue())
                .orElse(defaultValue);
    }


    @Transactional(readOnly = true)
    public List<RetirementPensionCompanySummaryDto> getAllCompany() {
        return retirementPensionJpaRepository.findAll()
                .stream()
                .collect(Collectors.toMap(
                        companyYield -> companyYield.getCompany(),
                        Function.identity(),
                        (existing, replacement) -> existing
                ))
                .values()
                .stream()
                .map(companyYield -> RetirementPensionCompanySummaryDto.builder()
                        .companyId(companyYield.getCompanyId())
                        .company(companyYield.getCompany())
                        .build()
                )
                .collect(Collectors.toList());
    }
}

