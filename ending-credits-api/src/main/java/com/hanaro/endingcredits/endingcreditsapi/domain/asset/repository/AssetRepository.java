package com.hanaro.endingcredits.endingcreditsapi.domain.asset.repository;

import com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities.AssetEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.enums.AssetType;
import com.hanaro.endingcredits.endingcreditsapi.domain.member.entities.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AssetRepository extends JpaRepository<AssetEntity, UUID> {
    List<AssetEntity> findAssetTypeAndAmountByMember(MemberEntity member);

    Optional<AssetEntity> findByMemberAndAssetType(MemberEntity memberEntity, AssetType assetType);

    @Query("SELECT a FROM AssetEntity a WHERE a.member = :member AND a.assetType = 'DEPOSIT'")
    List<AssetEntity> findAssetByMember(MemberEntity member);
}
