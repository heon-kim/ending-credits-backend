package com.hanaro.endingcredits.endingcreditsapi.domain.asset.repository.bank;


import com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities.bank.BankEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities.bank.FundEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.member.entities.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FundRepository extends JpaRepository<FundEntity, UUID> {
    List<FundEntity> findByBankAndAsset_Member(BankEntity bank, MemberEntity member);
    List<FundEntity> findByAsset_MemberAndIsConnectedTrue(MemberEntity member);
}
