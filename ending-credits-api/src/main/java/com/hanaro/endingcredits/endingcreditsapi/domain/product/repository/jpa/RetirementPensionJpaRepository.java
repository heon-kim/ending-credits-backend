package com.hanaro.endingcredits.endingcreditsapi.domain.product.repository.jpa;

import com.hanaro.endingcredits.endingcreditsapi.domain.product.entities.ProductArea;
import com.hanaro.endingcredits.endingcreditsapi.domain.product.entities.RetirementPensionProductEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.product.entities.SysType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RetirementPensionJpaRepository extends JpaRepository<RetirementPensionProductEntity, UUID> {
    Optional<RetirementPensionProductEntity> findByProductNameAndCompany(String productName, String company);
    List<RetirementPensionProductEntity> findByProductAreaAndSysType(ProductArea productArea, SysType sysType);
}
