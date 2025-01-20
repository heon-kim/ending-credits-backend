package com.hanaro.endingcredits.endingcreditsapi.domain.asset.service;

import com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities.etc.CashEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.repository.etc.CashRepository;
import com.hanaro.endingcredits.endingcreditsapi.domain.member.entities.MemberEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.member.repository.MemberRepository;
import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.code.status.ErrorStatus;
import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.exception.handler.AssetHandler;
import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.exception.handler.MemberHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CashService {
    private final MemberRepository memberRepository;
    private final CashRepository cashRepository;

    public BigDecimal getCashBalanceByMemberId(UUID memberId) {
        MemberEntity memberEntity = memberRepository.findByMemberId(memberId)
                        .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        CashEntity cashEntity = cashRepository.findByAsset_Member_MemberId(memberId)
                .orElseThrow(() -> new AssetHandler(ErrorStatus.CASH_NOT_FOUND));
        return cashEntity.getAmount();
    }
}