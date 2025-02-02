package com.hanaro.endingcredits.endingcreditsapi.domain.product.repository.elasticsearch;

import com.hanaro.endingcredits.endingcreditsapi.domain.product.entities.ProductArea;
import com.hanaro.endingcredits.endingcreditsapi.domain.product.entities.RetirementPensionSearchItems;
import com.hanaro.endingcredits.endingcreditsapi.domain.product.entities.SysType;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.List;

@Repository
public interface RetirementPensionSearchRepository extends ElasticsearchRepository<RetirementPensionSearchItems, String> {
    List<RetirementPensionSearchItems> findByCompanyContaining(String company);
}

