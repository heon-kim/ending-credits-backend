package com.hanaro.endingcredits.endingcreditsapi.domain.product.repository.elasticsearch;

import com.hanaro.endingcredits.endingcreditsapi.domain.product.entities.PensionSavingsEsEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.product.entities.ProductArea;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PensionSavingsSearchRepository extends ElasticsearchRepository<PensionSavingsEsEntity, String> {
    List<PensionSavingsEsEntity> findByProductNameContainingAndProductArea(
            String productName, String productArea
    );
}