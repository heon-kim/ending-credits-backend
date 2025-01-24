package com.hanaro.endingcredits.endingcreditsapi.domain.asset.service;

import com.hanaro.endingcredits.endingcreditsapi.domain.asset.dto.SecuritiesAssetDto;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.repository.securities.SecuritiesAccountRepository;
import com.hanaro.endingcredits.endingcreditsapi.domain.member.entities.MemberEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.member.repository.MemberRepository;
import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.code.status.ErrorStatus;
import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.exception.handler.MemberHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
    }
}



