package com.hanaro.endingcredits.endingcreditsapi.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ElasticsearchInitService {

    private final ElasticsearchOperations elasticsearchOperations;

    /**
     * 특정 인덱스 삭제 후 재생성
     */
    public void resetIndex(String indexName, Class<?> entityClass) {
        IndexOperations indexOperations = elasticsearchOperations.indexOps(IndexCoordinates.of(indexName));

        if (indexOperations.exists()) {
            indexOperations.delete();  // 기존 인덱스 삭제
        }

        indexOperations.create(); // 새 인덱스 생성
        indexOperations.putMapping(indexOperations.createMapping(entityClass)); // 매핑 적용
    }
}