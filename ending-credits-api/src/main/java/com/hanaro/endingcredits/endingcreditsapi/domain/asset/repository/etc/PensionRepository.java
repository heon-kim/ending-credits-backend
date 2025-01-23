package com.hanaro.endingcredits.endingcreditsapi.domain.asset.repository.etc;

import com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities.etc.PensionEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.member.entities.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PensionRepository extends JpaRepository<PensionEntity, UUID> {
    List<PensionEntity> findByAsset_Member(MemberEntity member);
    List<PensionEntity> findByAsset_MemberAndIsConnectedTrue(MemberEntity member);
}
