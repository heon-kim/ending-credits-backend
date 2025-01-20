package com.hanaro.endingcredits.endingcreditsapi.utils.config;

//import com.hanaro.endingcredits.endingcreditsapi.domain.product.mapper.PensionSavingsProductMapper;
import com.hanaro.endingcredits.endingcreditsapi.domain.product.repository.elasticsearch.PensionSavingsSearchRepository;
import com.hanaro.endingcredits.endingcreditsapi.domain.product.repository.jpa.PensionSavingsJpaRepository;
import com.hanaro.endingcredits.endingcreditsapi.domain.product.service.PensionSavingsService;
import com.hanaro.endingcredits.endingcreditsapi.domain.product.service.RetirementPensionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class RetirementPensionDataInitializer {

    private final PensionSavingsService pensionSavingsService;
    private final RetirementPensionService retirementPensionService;

    @Value("${api.key}")
    private String apiKey;

    private static final String API_URL = "https://www.fss.or.kr/openapi/api/psProdList.json";

    @Bean
    public ApplicationRunner initPensionSavingsData() {
        return args -> {
            List<Integer> areaCodes = List.of(1, 3, 4, 5);

            for (int areaCode : areaCodes) {
                int year = 2024;
                int quarter = 3;

                String requestUrl = UriComponentsBuilder.fromHttpUrl(API_URL)
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
    public ApplicationRunner initRetirementPensionData() {
        return args -> {
            List<Integer> areaCodes = List.of(1, 3, 4, 5); // 기본 지역 코드 리스트
            String reportDate = "2024/07";  // 기본 보고 날짜

            System.out.println("애플리케이션 실행 시 모든 지역 및 시스템 타입 데이터를 저장합니다...");

            for (int areaCode : areaCodes) {
                for (int sysTypeCode = 1; sysTypeCode <= 3; sysTypeCode++) {
                    System.out.println("지역 코드: " + areaCode + ", 시스템 타입 코드: " + sysTypeCode + " 데이터 저장 시작...");
                    retirementPensionService.savePensionData(areaCode, sysTypeCode, reportDate);
                    System.out.println("지역 코드: " + areaCode + ", 시스템 타입 코드: " + sysTypeCode + " 데이터 저장 완료!");
                }
            }

            System.out.println("모든 초기 데이터 저장이 완료되었습니다!");
        };
    }
}
