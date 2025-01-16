package com.hanaro.endingcredits.endingcreditsapi.utils.config;

import com.hanaro.endingcredits.endingcreditsapi.domain.product.service.PensionSavingsService;
import com.hanaro.endingcredits.endingcreditsapi.domain.product.service.RetirementPensionService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class RetirementPensionDataInitializer {

    private final RetirementPensionService retirementPensionService;

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
