package com.hanaro.endingcredits.endingcreditsapi.domain.product.repository;

import com.hanaro.endingcredits.endingcreditsapi.domain.product.entities.PensionSavingsProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PensionSavingsRepository extends JpaRepository<PensionSavingsProductEntity, UUID> {
}
