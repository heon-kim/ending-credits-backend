package com.hanaro.endingcredits.endingcreditsapi.domain.product.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class ElasticsearchInitService {

    private final ElasticsearchOperations elasticsearchOperations;

    /**
     * 특정 인덱스를 삭제 후 재생성하고, 커스텀 분석기를 적용
     */
    public void resetIndex(String indexName, Class<?> entityClass) {
        IndexOperations indexOperations = elasticsearchOperations.indexOps(IndexCoordinates.of(indexName));

        if (indexOperations.exists()) {
            indexOperations.delete();  // 기존 인덱스 삭제
        }

        // 인덱스 생성 + 커스텀 분석기 적용
        indexOperations.create(getIndexSettings());

        // 매핑 적용
        indexOperations.putMapping(indexOperations.createMapping(entityClass));
    }

    /**
     * 커스텀 분석기가 포함된 Elasticsearch 인덱스 설정 반환
     */
    private Map<String, Object> getIndexSettings() {
        return Map.of(
                "index", Map.of(
                        "analysis", Map.of(
                                "tokenizer", Map.of(
                                        "edge_ngram_tokenizer", Map.of(
                                                "type", "edge_ngram",
                                                "min_gram", 2,
                                                "max_gram", 10,
                                                "token_chars", new String[]{"letter", "digit"}  // <<== 필수 추가
                                        )
                                ),
                                "analyzer", Map.of(
                                        "edge_ngram_analyzer", Map.of(
                                                "type", "custom",
                                                "tokenizer", "edge_ngram_tokenizer"
                                        )
                                )
                        )
                )
        );
    }
}
