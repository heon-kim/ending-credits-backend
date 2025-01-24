package com.hanaro.endingcredits.endingcreditsapi.domain.product.repository.elasticsearch;

import com.hanaro.endingcredits.endingcreditsapi.domain.product.entities.PensionSavingsSearchItems;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PensionSavingsSearchRepository extends ElasticsearchRepository<PensionSavingsSearchItems, String> {
    List<PensionSavingsSearchItems> findByProductNameContainingAndProductArea(
            String productName, String productArea
    );
}