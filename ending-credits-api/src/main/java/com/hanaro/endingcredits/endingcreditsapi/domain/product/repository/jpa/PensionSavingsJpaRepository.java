package com.hanaro.endingcredits.endingcreditsapi.domain.product.repository.jpa;

import com.hanaro.endingcredits.endingcreditsapi.domain.product.entities.PensionSavingsProductEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.product.entities.ProductArea;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PensionSavingsJpaRepository extends JpaRepository<PensionSavingsProductEntity, UUID> {
    Slice<PensionSavingsProductEntity> findAllBy(Pageable pageable);
    List<PensionSavingsProductEntity> findByProductArea(ProductArea productArea);
    List<PensionSavingsProductEntity> findByCompany(String company);
    @Query("SELECT p FROM PensionSavingsProductEntity p WHERE p.productName LIKE %:productName% AND p.productArea = :productArea")
    List<PensionSavingsProductEntity> findByProductNameLikeAndProductArea(String productName, ProductArea productArea);

}