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

@Service
@Slf4j
@RequiredArgsConstructor
public class RetirementPensionService {
    private final ProductMapper productMapper;
    private final RestTemplate restTemplate;
    private final RetirementPensionJpaRepository retirementPensionJpaRepository;
    private final RetirementPensionEsRepository retirementPensionEsRepository;

    @Transactional
    public void fetchAndSaveAnnuityProducts(String apiUrl, int sysType) {
        RetirementPensionResponse response = restTemplate.getForObject(apiUrl, RetirementPensionResponse.class);
        if (response == null) return;

        if (response.getList() != null && !response.getList().isEmpty()) {
            for (CompanyDto companyList : response.getList()) {
                String company = companyList.getCompany();
                String area = companyList.getArea();
                List<Map<String, Object>> yieldDetail = companyList.getList();

                if (company == null || area == null || yieldDetail == null || yieldDetail.isEmpty()) {
                    log.warn("company 또는 area 또는 yieldDetail가 존재하지 않습니다.");
                    continue;
                }

                ProductArea yieldArea = ProductArea.fromDescription(area);
                SysType convertedSysType = SysType.fromCode(sysType);

                RetirementPensionYieldEntity entity = RetirementPensionYieldEntity.builder()
                        .company(company)
                        .area(yieldArea)
                        .yieldDetail(yieldDetail)
                        .sysType(convertedSysType)
                        .build();
                retirementPensionJpaRepository.save(entity);
//                PensionSavingsEsEntity document = productMapper.toPensionSavingsEsEntity(entity);
//                retirementPensionEsRepository.save(document);
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

    @Transactional(readOnly = true)
    public List<RetirementPensionEsEntity> searchProducts(String keyword, int areaCode, int sysTypeCode) {
        ProductArea productArea = ProductArea.fromCode(areaCode);
        SysType sysType = SysType.fromCode(sysTypeCode);

        return retirementPensionEsRepository.findByProductNameContainingAndProductAreaAndSysType(keyword, productArea, sysType);
    }

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
    public RetirementPensionDetailResponseDto getPensionProductDetailById(UUID companyYieldId) {
        RetirementPensionYieldEntity yieldEntity = retirementPensionJpaRepository.findById(companyYieldId)
                .orElseThrow(() -> new ProductHandler(ErrorStatus.PRODUCT_NOT_FOUND));

        Map<String, Object> yieldDetail = yieldEntity.getYieldDetail().get(0);

        return RetirementPensionDetailResponseDto.builder()
                .company(yieldEntity.getCompany())
                .area(yieldEntity.getArea().getDescription())
                .dbEarnRate(getDoubleValue(yieldDetail, "dbEarnRate", 0.0))
                .dbEarnRate3(getDoubleValue(yieldDetail, "dbEarnRate3", 0.0))
                .dbEarnRate5(getDoubleValue(yieldDetail, "dbEarnRate5", 0.0))
                .dbEarnRate7(getDoubleValue(yieldDetail, "dbEarnRate7", 0.0))
                .dbEarnRate10(getDoubleValue(yieldDetail, "dbEarnRate10", 0.0))
                .dcEarnRate(getDoubleValue(yieldDetail, "dcEarnRate", 0.0))
                .dcEarnRate3(getDoubleValue(yieldDetail, "dcEarnRate3", 0.0))
                .dcEarnRate5(getDoubleValue(yieldDetail, "dcEarnRate5", 0.0))
                .dcEarnRate7(getDoubleValue(yieldDetail, "dcEarnRate7", 0.0))
                .dcEarnRate10(getDoubleValue(yieldDetail, "dcEarnRate10", 0.0))
                .irpEarnRate(getDoubleValue(yieldDetail, "irpEarnRate", 0.0))
                .irpEarnRate3(getDoubleValue(yieldDetail, "irpEarnRate3", 0.0))
                .irpEarnRate5(getDoubleValue(yieldDetail, "irpEarnRate5", 0.0))
                .irpEarnRate7(getDoubleValue(yieldDetail, "irpEarnRate7", 0.0))
                .irpEarnRate10(getDoubleValue(yieldDetail, "irpEarnRate10", 0.0))
                .build();
    }


    private double getDoubleValue(Map<String, Object> map, String key, double defaultValue) {
        return Optional.ofNullable(map.get(key))
                .filter(value -> value instanceof Number)
                .map(value -> ((Number) value).doubleValue())
                .orElse(defaultValue);
    }


//    @Transactional(readOnly = true)
//    public List<RetirementPensionProductSummaryDto> getAllCompany() {
//        return retirementPensionJpaRepository.findAll()
//                .stream()
//                .map(product -> new RetirementPensionProductSummaryDto(product.getProductId(), product.getProductName()))
//                .collect(Collectors.toList());
//    }
}

