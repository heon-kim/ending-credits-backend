package com.hanaro.endingcredits.endingcreditsapi.domain.will.repository;

import com.hanaro.endingcredits.endingcreditsapi.domain.member.entities.MemberEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.will.entities.WillEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WillRepository extends JpaRepository<WillEntity, UUID> {
    WillEntity findByMember(MemberEntity member);
}
