package com.hanaro.endingcredits.endingcreditsapi.domain.asset.service;

import com.hanaro.endingcredits.endingcreditsapi.domain.asset.dto.BankAssetDto;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.enums.AssetType;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.repository.bank.DepositRepository;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.repository.bank.FundRepository;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.repository.bank.TrustRepository;
import com.hanaro.endingcredits.endingcreditsapi.domain.member.entities.MemberEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.member.repository.MemberRepository;
import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.code.status.ErrorStatus;
import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.exception.handler.MemberHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BankAssetService {

    private final DepositRepository depositRepository;
    private final TrustRepository trustRepository;
    private final FundRepository fundRepository;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public List<BankAssetDto> getConnectedBankAssets(UUID memberId) {
        MemberEntity member = getMember(memberId);

        List<BankAssetDto> connectedAssets = new ArrayList<>();

        // 예금 자산 조회
        List<BankAssetDto> depositAssets = depositRepository.findByAsset_MemberAndIsConnectedTrue(member)
                .stream()
                .map(deposit -> BankAssetDto.builder()
                        .bankName(deposit.getBank().getBankName())
                        .assetType(AssetType.DEPOSIT.getDescription())
                        .accountName(deposit.getAccountName())
                        .accountNumber(deposit.getAccountNumber())
                        .amount(deposit.getAmount())
                        .profitRate(null) // 예금/신탁에는 수익률 없음
                        .build())
                .collect(Collectors.toList());
        connectedAssets.addAll(depositAssets);

        // 신탁 자산 조회
        List<BankAssetDto> trustAssets = trustRepository.findByAsset_MemberAndIsConnectedTrue(member)
                .stream()
                .map(trust -> BankAssetDto.builder()
                        .bankName(trust.getBank().getBankName())
                        .assetType(AssetType.TRUST.getDescription())
                        .accountName(trust.getAccountName())
                        .accountNumber(trust.getAccountNumber())
                        .amount(trust.getAmount())
                        .profitRate(null) // 예금/신탁에는 수익률 없음
                        .build())
                .collect(Collectors.toList());
        connectedAssets.addAll(trustAssets);

        // 펀드 자산 조회
        List<BankAssetDto> fundAssets = fundRepository.findByAsset_MemberAndIsConnectedTrue(member)
                .stream()
                .map(fund -> BankAssetDto.builder()
                        .bankName(fund.getBank().getBankName())
                        .assetType(AssetType.FUND.getDescription())
                        .accountName(fund.getAccountName())
                        .accountNumber(fund.getAccountNumber())
                        .amount(fund.getFundAmount()) // 펀드 평가 금액
                        .profitRate(fund.getProfitRatio()) // 수익률
                        .build())
                .collect(Collectors.toList());
        connectedAssets.addAll(fundAssets);

        return connectedAssets;
    }

    private MemberEntity getMember(UUID memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
    }
}


