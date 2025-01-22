package com.hanaro.endingcredits.endingcreditsapi.domain.asset.service;

import com.hanaro.endingcredits.endingcreditsapi.domain.asset.dto.SecuritiesAssetDto;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.enums.CurrencyCodeType;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.repository.securities.SecuritiesAccountRepository;
import com.hanaro.endingcredits.endingcreditsapi.domain.member.entities.MemberEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class SecuritiesAssetService {

    private final SecuritiesAccountRepository securitiesAccountRepository;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public List<SecuritiesAssetDto> getSecuritiesAssets(UUID memberId) {
        MemberEntity member = getMember(memberId);

        // 전체 증권 계좌 조회 (isConnected가 true인 것만)
        return securitiesAccountRepository.findByAsset_MemberAndIsConnectedTrue(member)
                .stream()
                .map(account -> SecuritiesAssetDto.builder()
                        .securitiesCompanyName(account.getSecuritiesCompany().getSecuritiesCompanyName())
                        .stockName(account.getSecuritiesAccountName()) // 주식 이름
                        .accountNumber(account.getSecuritiesAccountNumber())
                        .amount(account.getPrincipal()) // 잔액 (원금)
                        .profitRate(account.getProfitRatio()) // 수익률
                        .currencyCode(account.getCurrencyCode().name()) // 통화 코드 (KRW, USD)
                        .build())
                .collect(Collectors.toList());
    }

    private MemberEntity getMember(UUID memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다. ID: " + memberId));
    }
}



