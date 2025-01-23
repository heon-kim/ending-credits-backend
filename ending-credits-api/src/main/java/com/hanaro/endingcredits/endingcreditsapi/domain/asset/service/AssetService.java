package com.hanaro.endingcredits.endingcreditsapi.domain.asset.service;

import com.hanaro.endingcredits.endingcreditsapi.domain.asset.dto.AssetsDetailDto;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.dto.AssetsLoanDetailDto;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.dto.LoanDetailDto;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities.AssetEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.enums.AssetType;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.repository.AssetRepository;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.repository.LoanRepository;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.repository.bank.DepositRepository;
import com.hanaro.endingcredits.endingcreditsapi.domain.member.entities.MemberEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.member.repository.MemberRepository;
import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.code.status.ErrorStatus;
import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.exception.handler.MemberHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class AssetService {
    private final AssetRepository assetRepository;
    private final MemberRepository memberRepository;
    private final DepositRepository depositRepository;
    private final LoanRepository loanRepository;

    /**
     * 연결된 자산 모두 조회 (대출 상세 제외)
     */
    public AssetsDetailDto getAssetTotalDetail(UUID memberId) {
        MemberEntity member = getMember(memberId);

        List<AssetEntity> assetsList = assetRepository.findAssetTypeAndAmountByMember(member);

        Map<AssetType, Long> totalAmounts = new EnumMap<>(AssetType.class);
        Long assetTotalAmount = 0L;

        for (AssetType type : AssetType.values()) {
            totalAmounts.put(type, 0L);
        }

        for (AssetEntity asset : assetsList) {
            totalAmounts.put(asset.getAssetType(),
                    totalAmounts.get(asset.getAssetType()) + asset.getAmount());
            assetTotalAmount += asset.getAmount();
        }

        return AssetsDetailDto.builder()
                .bank(formatAmount(
                        totalAmounts.get(AssetType.DEPOSIT) +
                                totalAmounts.get(AssetType.FUND) +
                                totalAmounts.get(AssetType.TRUST)))
                .securityCompany(formatAmount(totalAmounts.get(AssetType.SECURITIES)))
                .virtual(formatAmount(totalAmounts.get(AssetType.VIRTUAL_ASSET)))
                .realEstate(formatAmount(totalAmounts.get(AssetType.REAL_ESTATE)))
                .car(formatAmount(totalAmounts.get(AssetType.CAR)))
                .pension(formatAmount(totalAmounts.get(AssetType.PENSION)))
                .cash(formatAmount(totalAmounts.get(AssetType.CASH)))
                .assetTotal(formatAmount(assetTotalAmount)).build();
    }

    /**
     * 연결된 자산 모두 조회 (개인 자산 상세 페이지)
     */
    public AssetsLoanDetailDto getAssetsLoanDetail(UUID memberId) {
        MemberEntity member = getMember(memberId);

        AssetsDetailDto assetsDetail = getAssetTotalDetail(memberId);
        List<AssetEntity> depositAssetList = assetRepository.findAssetByMember(member);

        AtomicLong loanTotalAmount = new AtomicLong(0L);

        List<LoanDetailDto> loanDetailList = depositAssetList.stream()
            .flatMap(asset -> depositRepository.findByAsset(asset).stream())
            .flatMap(deposit -> loanRepository.findByDeposit(deposit).stream()
                    .peek(loan -> loanTotalAmount.addAndGet(loan.getLoanAmount().longValue()))
                    .map(loan -> LoanDetailDto.builder()
                            .totalAmount(formatAmount(loan.getTotalAmount().longValue()))
                            .loanAmount(formatAmount(loan.getLoanAmount().longValue()))
                            .expiryRemainDay(String.valueOf(loan.getExpiryDate().toEpochDay() - LocalDate.now().toEpochDay()))
                            .accountName(deposit.getAccountName())
                            .accountNumber(deposit.getAccountNumber())
                            .bankName(deposit.getBank().getBankName())
                            .build())
            )
            .toList();

        return AssetsLoanDetailDto.builder()
                .assetsDetail(assetsDetail)
                .loan(loanDetailList)
                .loanTotal(formatAmount(Long.valueOf(loanTotalAmount.get())))
                .build();
    }

    private String formatAmount(Long amount) {
        NumberFormat formatter = NumberFormat.getNumberInstance(Locale.KOREA);
        return formatter.format(amount);
    }

    private MemberEntity getMember(UUID memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
    }
}
