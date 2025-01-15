package com.hanaro.endingcredits.endingcreditsapi.domain.product.repository;

import com.hanaro.endingcredits.endingcreditsapi.domain.product.entities.ProductArea;
import com.hanaro.endingcredits.endingcreditsapi.domain.product.entities.RetirementPensionProductEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.product.entities.SysType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RetirementPensionRepository extends JpaRepository<RetirementPensionProductEntity, UUID> {
    Optional<RetirementPensionProductEntity> findByProductNameAndCompany(String productName, String company);
    List<RetirementPensionProductEntity> findByProductAreaAndSysType(ProductArea productArea, SysType sysType);
}
