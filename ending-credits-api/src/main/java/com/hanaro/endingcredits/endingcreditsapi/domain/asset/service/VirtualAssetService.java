package com.hanaro.endingcredits.endingcreditsapi.domain.asset.service;


import com.hanaro.endingcredits.endingcreditsapi.domain.asset.dto.VirtualAssetDto;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.repository.virtual.VirtualAssetRepository;
import com.hanaro.endingcredits.endingcreditsapi.domain.member.entities.MemberEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VirtualAssetService {

    private final VirtualAssetRepository virtualAssetRepository;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public List<VirtualAssetDto> getConnectedVirtualAssets(UUID memberId) {
        MemberEntity member = getMember(memberId);

        return virtualAssetRepository.findByAsset_MemberAndIsConnectedTrue(member)
                .stream()
                .map(asset -> VirtualAssetDto.builder()
                        .virtualAssetId(asset.getVirtualAssetId())
                        .exchangeName(asset.getExchange().getExchangeName())
                        .virtualAssetName(asset.getVirtualAssetName())
                        .quantity(asset.getQuantity())
                        .purchasePrice(asset.getPurchasePrice())
                        .currentPrice(asset.getCurrentPrice())
                        .profitRatio(asset.getProfitRatio()) // 수익률
                        .totalValue(asset.getTotalValue())
                        .build())
                .collect(Collectors.toList());
    }

    private MemberEntity getMember(UUID memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다. ID: " + memberId));
    }
}
