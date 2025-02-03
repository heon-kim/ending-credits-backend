package com.hanaro.endingcredits.endingcreditsapi.domain.product.service;

import com.hanaro.endingcredits.endingcreditsapi.domain.product.dto.*;
import com.hanaro.endingcredits.endingcreditsapi.domain.product.entities.*;
import com.hanaro.endingcredits.endingcreditsapi.domain.product.repository.elasticsearch.RetirementPensionSearchRepository;
import com.hanaro.endingcredits.endingcreditsapi.domain.product.repository.jpa.RetirementPensionJpaRepository;
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

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class RetirementPensionService {
    private final ProductMapper productMapper;
    private final RestTemplate restTemplate;
    private final RetirementPensionJpaRepository retirementPensionJpaRepository;
    private final RetirementPensionSearchRepository retirementPensionSearchRepository;

    @Transactional
    public void fetchAndSaveAnnuityYields(String apiUrl) {
        RetirementPensionResponse response = restTemplate.getForObject(apiUrl, RetirementPensionResponse.class);
        if (response == null) return;

        if (response.getList() != null && !response.getList().isEmpty()) {
            for (CompanyDto companyList: response.getList()) {
                Optional<RetirementPensionCompanyEntity> existingEntity = retirementPensionJpaRepository.findByCompany(companyList.getCompany());

                if (existingEntity.isPresent()) {
                    // 기존 엔티티 업데이트
                    RetirementPensionCompanyEntity entity = existingEntity.get();
                    List<Map<String, Object>> yieldDetails = entity.getYieldDetails();
                    yieldDetails.add((companyList.getList()).get(0));
                    retirementPensionJpaRepository.save(entity);
                } else {
                    String company = companyList.getCompany();
                    String area = companyList.getArea();
                    List<Map<String, Object>> yieldDetails = companyList.getList();

                    if (company == null || area == null || yieldDetails == null || yieldDetails.isEmpty()) {
                        log.warn("company 또는 area 또는 yieldDetail가 존재하지 않습니다.");
                        continue;
                    }

                    ProductArea yieldArea = ProductArea.fromDescription(area);

                    RetirementPensionCompanyEntity entity = RetirementPensionCompanyEntity.builder()
                            .company(company)
                            .area(yieldArea)
                            .yieldDetails(yieldDetails)
                            .build();
                    retirementPensionJpaRepository.save(entity);

                    RetirementPensionSearchItems items = productMapper.toRetirementPensionSearchItems(entity);
                    retirementPensionSearchRepository.save(items);
                }
            }
        }
    }

    @Transactional
    public void fetchAndSaveAnnuityFees(String apiUrl) {
        RetirementPensionFeeResponse response = restTemplate.getForObject(apiUrl, RetirementPensionFeeResponse.class);
        if (response == null) return;

        if (response.getList() != null && !response.getList().isEmpty()) {
            List<Map<String, Object>> list = response.getList();

            for (Map<String, Object> feeDetails : list) {
                Optional<RetirementPensionCompanyEntity> existingEntity = retirementPensionJpaRepository.findByCompany((String) feeDetails.get("company"));

                if (existingEntity.isPresent()) {
                    RetirementPensionCompanyEntity companyEntity = existingEntity.get();
                    List<Map<String, Object>> feeDetail = companyEntity.getFeeDetails();
                    if (feeDetail == null) {
                        feeDetail = new ArrayList<>();
                    }

                    Map<String, Object> filteredDetails = new HashMap<>();
                    for (Map.Entry<String, Object> entry : feeDetails.entrySet()) {
                        if (!entry.getKey().equals("area") && !entry.getKey().equals("company")) {
                            filteredDetails.put(entry.getKey(), entry.getValue());
                        }
                    }
                    feeDetail.add(filteredDetails);
                    companyEntity.setFeeDetails(feeDetail);
                    retirementPensionJpaRepository.save(companyEntity);
                }
            }
        }
    }

    @Transactional(readOnly = true)
    public RetirementPensionFeeComparisonDto getPensionComparisonDetail(UUID companyId) {
        RetirementPensionCompanyEntity companyEntity = retirementPensionJpaRepository.findById(companyId)
                .orElseThrow(() -> new FinanceHandler(ErrorStatus.PRODUCT_NOT_FOUND));

        List<Map<String, Object>> feeDetails = companyEntity.getFeeDetails();
        if (feeDetails == null || feeDetails.isEmpty()) {
            throw new FinanceHandler(ErrorStatus.FEE_DETAILS_NOT_FOUND);
        }

        Map<String, Object> feeDetail = feeDetails.get(0);

        return RetirementPensionFeeComparisonDto.builder()
                .companyId(companyId)
                .company(companyEntity.getCompany())
                .area(companyEntity.getArea().getDescription())
                .dbTotalCostRate((double) feeDetail.get("dbTotalCostRate"))
                .dbTotalFee((int) feeDetail.get("dbTotalFee"))
                .dbOprtMngFee((int) feeDetail.get("dbOprtMngFee"))
                .dbAsstMngFee((int) feeDetail.get("dbAsstMngFee"))
                .dbFundTotalCost((int) feeDetail.get("dbFundTotalCost"))
                .dcTotalCostRate((double) feeDetail.get("dcTotalCostRate"))
                .dcTotalFee((int) feeDetail.get("dcTotalFee"))
                .dcOprtMngFee((int) feeDetail.get("dcOprtMngFee"))
                .dcAsstMngFee((int) feeDetail.get("dcAsstMngFee"))
                .dcFundTotalCost((int) feeDetail.get("dcFundTotalCost"))
                .irpTotalCostRate((double) feeDetail.get("irpTotalCostRate"))
                .irpTotalFee((int) feeDetail.get("irpTotalFee"))
                .irpOprtMngFee((int) feeDetail.get("irpOprtMngFee"))
                .irpAsstMngFee((int) feeDetail.get("irpAsstMngFee"))
                .irpFundTotalCost((int) feeDetail.get("irpFundTotalCost"))
                .build();
    }

    @Transactional(readOnly = true)
    public RetirementPensionDetailResponseDto getPensionProductDetailById(UUID companyId) {
        RetirementPensionCompanyEntity companyEntity = retirementPensionJpaRepository.findById(companyId)
                .orElseThrow(() -> new FinanceHandler(ErrorStatus.PRODUCT_NOT_FOUND));

        List<Map<String, Object>> yieldDetails = companyEntity.getYieldDetails();

        YieldDetailDto guaranteedYieldDetails = YieldDetailDto.builder()
                .dbEarnRate(getDoubleValue(yieldDetails.get(0), "dbEarnRate"))
                .dbEarnRate3(getDoubleValue(yieldDetails.get(0), "dbEarnRate3"))
                .dbEarnRate5(getDoubleValue(yieldDetails.get(0), "dbEarnRate5"))
                .dbEarnRate7(getDoubleValue(yieldDetails.get(0), "dbEarnRate7"))
                .dbEarnRate10(getDoubleValue(yieldDetails.get(0), "dbEarnRate10"))
                .dcEarnRate(getDoubleValue(yieldDetails.get(0), "dcEarnRate"))
                .dcEarnRate3(getDoubleValue(yieldDetails.get(0), "dcEarnRate3"))
                .dcEarnRate5(getDoubleValue(yieldDetails.get(0), "dcEarnRate5"))
                .dcEarnRate7(getDoubleValue(yieldDetails.get(0), "dcEarnRate7"))
                .dcEarnRate10(getDoubleValue(yieldDetails.get(0), "dcEarnRate10"))
                .irpEarnRate(getDoubleValue(yieldDetails.get(0), "irpEarnRate"))
                .irpEarnRate3(getDoubleValue(yieldDetails.get(0), "irpEarnRate3"))
                .irpEarnRate5(getDoubleValue(yieldDetails.get(0), "irpEarnRate5"))
                .irpEarnRate7(getDoubleValue(yieldDetails.get(0), "irpEarnRate7"))
                .irpEarnRate10(getDoubleValue(yieldDetails.get(0), "irpEarnRate10"))
                .build();

        YieldDetailDto nonGuaranteedYieldDetails = YieldDetailDto.builder()
                .dbEarnRate(getDoubleValue(yieldDetails.get(1), "dbEarnRate"))
                .dbEarnRate3(getDoubleValue(yieldDetails.get(1), "dbEarnRate3"))
                .dbEarnRate5(getDoubleValue(yieldDetails.get(1), "dbEarnRate5"))
                .dbEarnRate7(getDoubleValue(yieldDetails.get(1), "dbEarnRate7"))
                .dbEarnRate10(getDoubleValue(yieldDetails.get(1), "dbEarnRate10"))
                .dcEarnRate(getDoubleValue(yieldDetails.get(1), "dcEarnRate"))
                .dcEarnRate3(getDoubleValue(yieldDetails.get(1), "dcEarnRate3"))
                .dcEarnRate5(getDoubleValue(yieldDetails.get(1), "dcEarnRate5"))
                .dcEarnRate7(getDoubleValue(yieldDetails.get(1), "dcEarnRate7"))
                .dcEarnRate10(getDoubleValue(yieldDetails.get(1), "dcEarnRate10"))
                .irpEarnRate(getDoubleValue(yieldDetails.get(1), "irpEarnRate"))
                .irpEarnRate3(getDoubleValue(yieldDetails.get(1), "irpEarnRate3"))
                .irpEarnRate5(getDoubleValue(yieldDetails.get(1), "irpEarnRate5"))
                .irpEarnRate7(getDoubleValue(yieldDetails.get(1), "irpEarnRate7"))
                .irpEarnRate10(getDoubleValue(yieldDetails.get(1), "irpEarnRate10"))
                .build();


        Map<String, YieldDetailDto> earnRates = Map.of(
                "원리금 보장", guaranteedYieldDetails,
                "원리금 비보장", nonGuaranteedYieldDetails
        );

        return RetirementPensionDetailResponseDto.builder()
                .company(companyEntity.getCompany())
                .area(companyEntity.getArea().getDescription())
                .earnRates(earnRates)
                .build();
    }

    private double getDoubleValue(Map<String, Object> map, String key) {
        return Optional.ofNullable(map.get(key))
                .filter(value -> value instanceof Number)
                .map(value -> ((Number) value).doubleValue())
                .orElseThrow(() -> new FinanceHandler(ErrorStatus.FEE_DETAILS_NOT_FOUND));
    }


    @Transactional(readOnly = true)
    public SliceResponse<RetirementPensionCompanySummaryDto> getAllCompany(Pageable pageable) {
        Slice<RetirementPensionCompanyEntity> retirementPensionCompanies = retirementPensionJpaRepository.findAllBy(pageable);

        Slice<RetirementPensionCompanySummaryDto> mappedCompanies = retirementPensionCompanies.map(companyYield ->
                        RetirementPensionCompanySummaryDto.builder()
                                .companyId(companyYield.getCompanyId())
                                .company(companyYield.getCompany())
                                .build()
                );

        return new SliceResponse<>(mappedCompanies);
    }

    @Transactional(readOnly = true)
    public List<RetirementPensionSearchItems> searchCompany(String keyword) {
        return retirementPensionSearchRepository.findByCompanyContaining(keyword);
    }

}

