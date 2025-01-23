package com.hanaro.endingcredits.endingcreditsapi.domain.asset.repository.bank;


import com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities.AssetEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities.bank.BankEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities.bank.TrustEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.member.entities.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TrustRepository extends JpaRepository<TrustEntity, UUID> {
    List<TrustEntity> findByBankAndAsset_Member(BankEntity bank, MemberEntity member);
    List<TrustEntity> findByAsset_MemberAndIsConnectedTrue(MemberEntity member);
    List<TrustEntity> findByAsset_Member(MemberEntity member);
}
