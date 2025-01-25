package com.hanaro.endingcredits.endingcreditsapi.domain.product.repository.jpa;

import com.hanaro.endingcredits.endingcreditsapi.domain.product.entities.PensionSavingsProductEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.product.entities.RetirementPensionCompanyEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RetirementPensionJpaRepository extends JpaRepository<RetirementPensionCompanyEntity, UUID> {
    @Query("SELECT DISTINCT r FROM RetirementPensionCompanyEntity r")
    Slice<RetirementPensionCompanyEntity> findAllBy(Pageable pageable);
    Optional<RetirementPensionCompanyEntity> findByCompany(String company);
}