package com.hanaro.endingcredits.endingcreditsapi.domain.member.repository;

import com.hanaro.endingcredits.endingcreditsapi.domain.member.entities.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface MemberRepository extends JpaRepository<MemberEntity, UUID> {
    @Query("SELECT m FROM member m WHERE m.identifier = :identifier AND m.isActive = true")
    Optional<MemberEntity> findByIdentifier(String identifier);

    Optional<MemberEntity> findByMemberId(UUID memberId);

    @Query("SELECT m FROM member m WHERE m.email = :email AND m.isActive = true")
    Optional<MemberEntity> findByEmail(String email);
}
