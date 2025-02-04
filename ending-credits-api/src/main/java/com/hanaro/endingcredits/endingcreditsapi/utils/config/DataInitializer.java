package com.hanaro.endingcredits.endingcreditsapi.utils.config;

//import com.hanaro.endingcredits.endingcreditsapi.domain.product.mapper.PensionSavingsProductMapper;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.service.AssetDataService;
import com.hanaro.endingcredits.endingcreditsapi.domain.product.entities.PensionSavingsSearchItems;
import com.hanaro.endingcredits.endingcreditsapi.domain.product.entities.RetirementPensionSearchItems;
import com.hanaro.endingcredits.endingcreditsapi.domain.product.service.PensionSavingsService;
import com.hanaro.endingcredits.endingcreditsapi.domain.product.service.RetirementPensionService;
import com.hanaro.endingcredits.endingcreditsapi.domain.product.service.ElasticsearchInitService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final PensionSavingsService pensionSavingsService;
    private final RetirementPensionService retirementPensionService;
    private final AssetDataService assetDataService;
    private final ElasticsearchInitService elasticsearchInitService;

    @Bean
    public ApplicationRunner initAssetData() {
        return args -> {
            System.out.println("애플리케이션 실행 시 자산 목 데이터를 생성합니다...");
            assetDataService.generateMockData(); // 자산 목데이터 생성 호출
            System.out.println("자산 목 데이터 생성이 완료되었습니다!");
        };
    }

    @Value("${api.key}")
    private String apiKey;

    private static final String PENSION_SAVINGS_API_URL = "https://www.fss.or.kr/openapi/api/psProdList.json";
    private static final String RETIREMENT_PENSION_API_URL = "https://www.fss.or.kr/openapi/api/rpCorpResultList.json";
    private static final String RETIREMENT_PENSION_FEE_API_URL = "https://www.fss.or.kr/openapi/api/rpCorpBurdenRatioList.json";

    @Bean
    public ApplicationRunner initPensionSavingsData() {
        return args -> {
            elasticsearchInitService.resetIndex("pension_savings_index", PensionSavingsSearchItems .class);

            List<Integer> areaCodes = List.of(1, 3, 4, 5);

            for (int areaCode : areaCodes) {
                int year = 2024;
                int quarter = 3;

                String requestUrl = UriComponentsBuilder.fromHttpUrl(PENSION_SAVINGS_API_URL)
                        .queryParam("key", apiKey)
                        .queryParam("year", year)
                        .queryParam("quarter", quarter)
                        .queryParam("areaCode", areaCode)
                        .toUriString();

                pensionSavingsService.fetchAndSavePensionProducts(requestUrl, areaCode);
            }
        };
    }

    @Bean
    public ApplicationRunner initRetirementPensionEarningRateData() {
        return args -> {
            elasticsearchInitService.resetIndex("retirement_pension_index", RetirementPensionSearchItems.class);

            int year = 2024;
            int quarter = 3;
            int feeYear = 2023;

            List<Integer> sysTypes = List.of(1, 2);

            for (int sysType : sysTypes) {
                String requestUrl = UriComponentsBuilder.fromHttpUrl(RETIREMENT_PENSION_API_URL)
                        .queryParam("key", apiKey)
                        .queryParam("year", year)
                        .queryParam("quarter", quarter)
                        .queryParam("sysType", sysType)
                        .toUriString();

                retirementPensionService.fetchAndSaveAnnuityYields(requestUrl);
            }

            String requestUrl = UriComponentsBuilder.fromHttpUrl(RETIREMENT_PENSION_FEE_API_URL)
                    .queryParam("key", apiKey)
                    .queryParam("year", feeYear)
                    .toUriString();

            retirementPensionService.fetchAndSaveAnnuityFees(requestUrl);
        };
    }
}
