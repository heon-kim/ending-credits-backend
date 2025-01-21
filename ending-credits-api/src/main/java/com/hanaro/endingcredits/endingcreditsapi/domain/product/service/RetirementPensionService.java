package com.hanaro.endingcredits.endingcreditsapi.domain.product.service;

import com.hanaro.endingcredits.endingcreditsapi.domain.product.dto.RetirementPensionProductDto;
import com.hanaro.endingcredits.endingcreditsapi.domain.product.dto.RetirementPensionProductSummaryDto;
import com.hanaro.endingcredits.endingcreditsapi.domain.product.dto.RetirementPensionResponse;
import com.hanaro.endingcredits.endingcreditsapi.domain.product.entities.RetirementPensionEsEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.product.entities.RetirementPensionProductEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.product.entities.ProductArea;
import com.hanaro.endingcredits.endingcreditsapi.domain.product.entities.SysType;
import com.hanaro.endingcredits.endingcreditsapi.domain.product.repository.elasticsearch.RetirementPensionEsRepository;
import com.hanaro.endingcredits.endingcreditsapi.domain.product.repository.jpa.RetirementPensionJpaRepository;
import com.hanaro.endingcredits.endingcreditsapi.utils.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RetirementPensionService {
    private final ProductMapper productMapper;
    private final RestTemplate restTemplate = new RestTemplate();
    private final RetirementPensionJpaRepository retirementPensionJpaRepository;
    private final RetirementPensionEsRepository retirementPensionEsRepository;

    private static final String API_URL = "https://www.fss.or.kr/openapi/api/rpGuaranteedProdList.json";

    @Value("${api.key}")
    private String apiKey;

    @Transactional
    public void savePensionData(int areaCode, int sysTypeCode, String reportDate) {
        String requestUrl = UriComponentsBuilder.fromHttpUrl(API_URL)
                .queryParam("key", apiKey)
                .queryParam("areaCode", areaCode)
                .queryParam("sysType", sysTypeCode)
                .queryParam("reportDate", reportDate)
                .toUriString();

        RetirementPensionResponse response = restTemplate.getForObject(requestUrl, RetirementPensionResponse.class);
        if (response == null || response.getList() == null || response.getList().isEmpty()) {
            System.out.println("응답값 받아오기 실패!");
            return;
        }

        ProductArea productArea = ProductArea.fromCode(areaCode);
        SysType sysType = SysType.fromCode(sysTypeCode);

        List<RetirementPensionProductDto> limitedList = response.getList()
                .stream()
                .limit(50)  // 리스트에서 처음 50개만 가져옴
                .collect(Collectors.toList());

        for (RetirementPensionProductDto dto : limitedList) {
            saveOrUpdate(dto, productArea, sysType);
        }
    }

    @Transactional
    public void saveOrUpdate(RetirementPensionProductDto dto, ProductArea productArea, SysType sysType) {
        Optional<RetirementPensionProductEntity> existingEntity =
                retirementPensionJpaRepository.findByProductNameAndCompany(dto.getProduct(), dto.getCompany());

        if (existingEntity.isPresent()) {
            RetirementPensionProductEntity entity = existingEntity.get();
            entity.update(productMapper.toRetirementPensionProductEntity(dto, productArea, sysType));
            retirementPensionJpaRepository.save(entity);
            retirementPensionEsRepository.save(productMapper.toRetirementPensionEsEntity(entity));
        } else {
            RetirementPensionProductEntity newEntity = productMapper.toRetirementPensionProductEntity(dto, productArea, sysType);
            retirementPensionJpaRepository.save(newEntity);
            retirementPensionEsRepository.save(productMapper.toRetirementPensionEsEntity(newEntity));
        }
    }

    @Transactional(readOnly = true)
    public List<RetirementPensionEsEntity> searchProducts(String keyword, int areaCode, int sysTypeCode) {
        ProductArea productArea = ProductArea.fromCode(areaCode);
        SysType sysType = SysType.fromCode(sysTypeCode);

        return retirementPensionEsRepository.findByProductNameContainingAndProductAreaAndSysType(keyword, productArea, sysType);
    }


    @Transactional(readOnly = true)
    public List<RetirementPensionProductSummaryDto> getPensionProducts(int areaCode, int sysTypeCode) {
        ProductArea productArea = ProductArea.fromCode(areaCode);
        SysType sysType = SysType.fromCode(sysTypeCode);

        return retirementPensionJpaRepository.findByProductAreaAndSysType(productArea, sysType)
                .stream()
                .map(product -> new RetirementPensionProductSummaryDto(product.getProductId(), product.getProductName()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<RetirementPensionProductEntity> getPensionProductById(UUID productId) {
        return retirementPensionJpaRepository.findById(productId);
    }

    @Transactional(readOnly = true)
    public List<RetirementPensionProductSummaryDto> getAllPensionProducts() {
        return retirementPensionJpaRepository.findAll()
                .stream()
                .map(product -> new RetirementPensionProductSummaryDto(product.getProductId(), product.getProductName()))
                .collect(Collectors.toList());
    }
}

