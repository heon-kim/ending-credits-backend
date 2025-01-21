package com.hanaro.endingcredits.endingcreditsapi.domain.asset.repository.virtual;

import com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities.virtual.ExchangeEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities.virtual.VirtualAsset;
import com.hanaro.endingcredits.endingcreditsapi.domain.member.entities.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VirtualAssetRepository extends JpaRepository<VirtualAsset, UUID> {
    List<VirtualAsset> findByExchangeAndAsset_Member(ExchangeEntity exchange, MemberEntity member);
}
