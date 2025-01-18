package com.hanaro.endingcredits.endingcreditsapi.domain.asset.service;

import com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities.AssetEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.repository.AssetRepository;
import com.hanaro.endingcredits.endingcreditsapi.domain.member.entities.MemberEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AssetConnectionService {

    private final MemberRepository memberRepository;
    private final AssetRepository assetRepository;

    @Transactional(readOnly = true)
    public boolean checkMemberAssetConnection(UUID memberId) {
        MemberEntity member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("회원 정보를 찾을 수 없습니다."));
        return member.isLinked();
    }

    @Transactional
    public void connectAssets(UUID memberId, List<UUID> assetIdsToConnect) {
        MemberEntity member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("회원 정보를 찾을 수 없습니다."));

        // 선택한 자산 ID들을 자산 테이블에서 조회
        List<AssetEntity> assets = assetRepository.findAllById(assetIdsToConnect);

        // 회원의 자산 목록에 추가
        member.getAssets().addAll(assets);

        // 연결 상태를 true로 설정
        member.setLinked(true);
        memberRepository.save(member);
    }

    @Transactional
    public void disconnectAssets(UUID memberId) {
        MemberEntity member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("회원 정보를 찾을 수 없습니다."));

        // 모든 자산 연결 해제
        member.getAssets().clear();

        // 연결 상태를 false로 설정
        member.setLinked(false);
        memberRepository.save(member);
    }
}
