package com.hanaro.endingcredits.endingcreditsapi.domain.product.controller;

import com.hanaro.endingcredits.endingcreditsapi.domain.product.service.PerformanceComparisonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/performance")
@RequiredArgsConstructor
@Slf4j
public class PerformanceComparisonController {

    private final PerformanceComparisonService performanceComparisonService;

    /**
     * PostgreSQL vs Elasticsearch 검색 성능 비교 (Keyword + AreaCode)
     */
    @GetMapping("/compare/read")
    public String compareReadPerformance(
            @RequestParam String keyword,
            @RequestParam int areaCode
    ) {
        performanceComparisonService.compareReadPerformance(keyword, areaCode);
        return "검색 성능 비교가 완료되었습니다. 로그에서 성능 결과를 확인하세요.";
    }
}
