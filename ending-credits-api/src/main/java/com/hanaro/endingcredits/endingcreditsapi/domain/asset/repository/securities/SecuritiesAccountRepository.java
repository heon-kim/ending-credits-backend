package com.hanaro.endingcredits.endingcreditsapi.domain.asset.repository.securities;

import com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities.securities.SecuritiesAccountEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities.securities.SecuritiesCompanyEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.member.entities.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SecuritiesAccountRepository extends JpaRepository<SecuritiesAccountEntity, UUID> {
    List<SecuritiesAccountEntity> findBySecuritiesCompanyAndAsset_Member(SecuritiesCompanyEntity company, MemberEntity member);
}
