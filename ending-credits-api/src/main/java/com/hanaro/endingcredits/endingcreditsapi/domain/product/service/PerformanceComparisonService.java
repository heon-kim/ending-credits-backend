package com.hanaro.endingcredits.endingcreditsapi.domain.product.service;

import com.hanaro.endingcredits.endingcreditsapi.domain.product.entities.PensionSavingsProductEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.product.entities.PensionSavingsSearchItems;
import com.hanaro.endingcredits.endingcreditsapi.domain.product.entities.ProductArea;
import com.hanaro.endingcredits.endingcreditsapi.domain.product.repository.elasticsearch.PensionSavingsSearchRepository;
import com.hanaro.endingcredits.endingcreditsapi.domain.product.repository.jpa.PensionSavingsJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PerformanceComparisonService {

    private final PensionSavingsJpaRepository pensionProductRepository;
    private final PensionSavingsSearchRepository pensionSavingsSearchRepository;

    /**
     * PostgreSQL LIKE 검색 성능 측정 (Keyword + AreaCode)
     */
    @Transactional(readOnly = true)
    public long testPostgreSQLLikeSearch(String keyword, int areaCode) {
        ProductArea productArea = ProductArea.fromCode(areaCode); // int -> String 변환

        long startTime = System.currentTimeMillis();

        List<PensionSavingsProductEntity> result = pensionProductRepository
                .findByProductNameLikeAndProductArea("%" + keyword + "%", productArea);  // LIKE 검색

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        log.info("PostgreSQL LIKE 검색 소요 시간: {}ms | 키워드: {} | 지역: {}", duration, keyword, productArea);
        return duration;
    }

    /**
     * Elasticsearch match 검색 성능 측정 (Keyword + AreaCode)
     */
    @Transactional(readOnly = true)
    public long testElasticsearchSearch(String keyword, int areaCode) {
        String productArea = ProductArea.fromCode(areaCode).getDescription(); // int -> String 변환

        long startTime = System.currentTimeMillis();

        List<PensionSavingsSearchItems> result = pensionSavingsSearchRepository
                .findByProductNameContainingAndProductArea(keyword, productArea);  // match 검색

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        log.info("Elasticsearch 검색 소요 시간: {}ms | 키워드: {} | 지역: {}", duration, keyword, productArea);
        return duration;
    }

    /**
     * PostgreSQL vs Elasticsearch 검색 성능 비교 (Keyword + AreaCode)
     */
    public void compareReadPerformance(String keyword, int areaCode) {
        log.info("=== 검색 성능 비교 시작 ===");
        long postgresTime = testPostgreSQLLikeSearch(keyword, areaCode);
        long elasticTime = testElasticsearchSearch(keyword, areaCode);
        log.info("PostgreSQL LIKE 검색: {}ms, Elasticsearch match 검색: {}ms", postgresTime, elasticTime);
    }
}
