package com.hanaro.endingcredits.endingcreditsapi.domain.product.repository;

import com.hanaro.endingcredits.endingcreditsapi.domain.product.entities.RetirementPensionProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RetirementPensionRepository extends JpaRepository<RetirementPensionProductEntity, UUID> {
    Optional<RetirementPensionProductEntity> findByProductNameAndCompany(String productName, String company);
}
