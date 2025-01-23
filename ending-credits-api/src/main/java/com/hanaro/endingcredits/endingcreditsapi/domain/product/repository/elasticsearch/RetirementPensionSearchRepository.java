package com.hanaro.endingcredits.endingcreditsapi.domain.product.repository.elasticsearch;

import com.hanaro.endingcredits.endingcreditsapi.domain.product.entities.ProductArea;
import com.hanaro.endingcredits.endingcreditsapi.domain.product.entities.RetirementPensionSearchItems;
import com.hanaro.endingcredits.endingcreditsapi.domain.product.entities.SysType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.List;

@Repository
public interface RetirementPensionEsRepository extends ElasticsearchRepository<RetirementPensionSearchItems, UUID> {
    List<RetirementPensionSearchItems> findByProductNameContainingAndProductAreaAndSysType(String productName, ProductArea productArea, SysType sysType);
}
