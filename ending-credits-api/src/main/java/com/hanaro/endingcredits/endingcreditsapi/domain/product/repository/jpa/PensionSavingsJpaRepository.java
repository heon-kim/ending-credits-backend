package com.hanaro.endingcredits.endingcreditsapi.domain.product.repository.jpa;

import com.hanaro.endingcredits.endingcreditsapi.domain.product.entities.PensionSavingsProductEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.product.entities.ProductArea;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PensionSavingsJpaRepository extends JpaRepository<PensionSavingsProductEntity, UUID> {
    Slice<PensionSavingsProductEntity> findAllBy(Pageable pageable);
    List<PensionSavingsProductEntity> findByProductArea(ProductArea productArea);
    List<PensionSavingsProductEntity> findByCompany(String company);
    @Query("SELECT p FROM PensionSavingsProductEntity p WHERE p.productName LIKE %:productName% AND p.productArea = :productArea")
    List<PensionSavingsProductEntity> findByProductNameLikeAndProductArea(String productName, ProductArea productArea);

    @Query("SELECT p FROM PensionSavingsProductEntity p LEFT JOIN FETCH p.productDetails WHERE p.productId = :id")
    Optional<PensionSavingsProductEntity> findByIdWithDetails(@Param("id") UUID id);

    @Query("SELECT p FROM PensionSavingsProductEntity p LEFT JOIN FETCH p.productDetails")
    List<PensionSavingsProductEntity> findAllWithDetails();

    // 전략별 최적화된 쿼리 추가
    @Query("""
        SELECT DISTINCT p FROM PensionSavingsProductEntity p 
        LEFT JOIN FETCH p.productDetails d 
        WHERE d.earnRate >= :minRate 
        ORDER BY d.earnRate DESC 
        LIMIT 100
    """)
    List<PensionSavingsProductEntity> findTopPerformingProducts(@Param("minRate") double minRate);

    @Query("""
        SELECT DISTINCT p FROM PensionSavingsProductEntity p 
        LEFT JOIN FETCH p.productDetails d 
        WHERE d.guarantees = :guarantees 
        ORDER BY d.avgEarnRate3 DESC 
        LIMIT 100
    """)
    List<PensionSavingsProductEntity> findStableProducts(@Param("guarantees") String guarantees);

    // 공격형 전략 (수익률 중심)
    @Query("""
        SELECT DISTINCT p, 
            (d.earnRate * 0.6 + d.earnRate1 * 0.2 + d.avgEarnRate10 * 0.2) as score 
        FROM PensionSavingsProductEntity p 
        LEFT JOIN FETCH p.productDetails d 
        WHERE d.earnRate >= :minRate 
        ORDER BY score DESC 
        LIMIT 10
    """)
    List<PensionSavingsProductEntity> findAggressiveProducts(@Param("minRate") double minRate);

    // 안정형 전략 (보장성 중심)
    @Query("""
        SELECT DISTINCT p, 
            (d.avgEarnRate5 * 0.4 + d.avgEarnRate3 * 0.4 - d.feeRate1 * 0.2) as score 
        FROM PensionSavingsProductEntity p 
        LEFT JOIN FETCH p.productDetails d 
        WHERE d.guarantees = 'Y' 
        ORDER BY score DESC 
        LIMIT 10
    """)
    List<PensionSavingsProductEntity> findStableProducts();

    // 단기형 전략
    @Query("""
        SELECT DISTINCT p, 
            (d.earnRate * 0.5 + d.earnRate1 * 0.3 + (d.reserve * 1.0 / NULLIF(d.balance, 0)) * 0.2) as score 
        FROM PensionSavingsProductEntity p 
        LEFT JOIN FETCH p.productDetails d 
        ORDER BY score DESC 
        LIMIT 10
    """)
    List<PensionSavingsProductEntity> findShortTermProducts();

    // 장기형 전략
    @Query("""
        SELECT DISTINCT p, 
            (d.avgEarnRate10 * 0.4 + d.avgEarnRate7 * 0.4 - d.feeRate1 * 0.2) as score 
        FROM PensionSavingsProductEntity p 
        LEFT JOIN FETCH p.productDetails d 
        WHERE d.avgEarnRate10 IS NOT NULL 
        ORDER BY score DESC 
        LIMIT 10
    """)
    List<PensionSavingsProductEntity> findLongTermProducts();

    // 저비용 전략
    @Query("""
        SELECT DISTINCT p, 
            (-d.feeRate1 * 0.5 - d.avgFeeRate3 * 0.3 + (d.reserve * 1.0 / NULLIF(d.balance, 0)) * 0.2) as score 
        FROM PensionSavingsProductEntity p 
        LEFT JOIN FETCH p.productDetails d 
        ORDER BY score DESC 
        LIMIT 10
    """)
    List<PensionSavingsProductEntity> findLowCostProducts();

    // 안정수익 전략
    @Query("""
        SELECT DISTINCT p, 
            (d.avgEarnRate10 * 0.4 - ABS(d.earnRate - d.avgEarnRate3) * 0.3) as score 
        FROM PensionSavingsProductEntity p 
        LEFT JOIN FETCH p.productDetails d 
        WHERE d.guarantees = 'Y' 
        ORDER BY score DESC 
        LIMIT 10
    """)
    List<PensionSavingsProductEntity> findStableProfitProducts();

    // 위험선호 전략
    @Query("""
        SELECT DISTINCT p, 
            (d.earnRate * 0.7 + ABS(d.earnRate1 - d.earnRate) * 0.3) as score 
        FROM PensionSavingsProductEntity p 
        LEFT JOIN FETCH p.productDetails d 
        WHERE d.guarantees = 'N' 
        ORDER BY score DESC 
        LIMIT 10
    """)
    List<PensionSavingsProductEntity> findRiskTolerantProducts();
}