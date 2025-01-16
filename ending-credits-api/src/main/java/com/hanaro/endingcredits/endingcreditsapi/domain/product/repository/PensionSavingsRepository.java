package com.hanaro.endingcredits.endingcreditsapi.domain.product.repository;

import com.hanaro.endingcredits.endingcreditsapi.domain.product.entities.PensionSavingsProductEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.product.entities.ProductArea;
import com.hanaro.endingcredits.endingcreditsapi.domain.product.entities.RetirementPensionProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PensionSavingsRepository extends JpaRepository<PensionSavingsProductEntity, UUID> {
    List<PensionSavingsProductEntity> findByProductArea(ProductArea productArea);
}