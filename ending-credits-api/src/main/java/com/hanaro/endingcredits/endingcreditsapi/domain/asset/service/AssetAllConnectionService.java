package com.hanaro.endingcredits.endingcreditsapi.domain.asset.service;

import com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities.AssetEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities.bank.DepositEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities.bank.FundEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities.bank.TrustEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities.etc.CarEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities.etc.CashEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities.etc.PensionEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities.etc.RealEstateEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities.securities.SecuritiesAccountEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities.virtual.VirtualAsset;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.enums.AssetType;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.repository.*;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.repository.bank.DepositRepository;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.repository.bank.FundRepository;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.repository.bank.TrustRepository;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.repository.etc.CarRepository;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.repository.etc.CashRepository;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.repository.etc.PensionRepository;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.repository.etc.RealEstateRepository;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.repository.securities.SecuritiesAccountRepository;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.repository.virtual.VirtualAssetRepository;
import com.hanaro.endingcredits.endingcreditsapi.domain.member.entities.MemberEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.member.repository.MemberRepository;
import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.exception.handler.AssetHandler;
import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.exception.handler.MemberHandler;
import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.code.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AssetAllConnectionService {

    private final MemberRepository memberRepository;
    private final DepositRepository depositRepository;
    private final FundRepository fundRepository;
    private final TrustRepository trustRepository;
    private final SecuritiesAccountRepository securitiesAccountRepository;
    private final VirtualAssetRepository virtualAssetRepository;
    private final CarRepository carRepository;
    private final RealEstateRepository realEstateRepository;
    private final PensionRepository pensionRepository;
    private final AssetRepository assetRepository;
    private final CashRepository cashRepository;

    /**
     * 회원의 전체 자산을 연결하고 총액을 계산 (isConnected = true)
     */
    @Transactional
    public void connectAllAssets(UUID memberId) {
        // 회원 확인
        MemberEntity member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        // 은행 자산 연결 및 총액 계산
        BigDecimal totalDeposits = connectAndCalculateDeposits(member);
        BigDecimal totalFunds = connectAndCalculateFunds(member);
        BigDecimal totalTrusts = connectAndCalculateTrusts(member);

        // 증권 자산 연결 및 총액 계산
        BigDecimal totalSecurities = connectAndCalculateSecuritiesAccounts(member);

        // 가상자산 연결 및 총액 계산
        BigDecimal totalVirtualAssets = connectAndCalculateVirtualAssets(member);

        // AssetEntity 업데이트
        updateAssetEntity(member, AssetType.DEPOSIT, totalDeposits);
        updateAssetEntity(member, AssetType.FUND, totalFunds);
        updateAssetEntity(member, AssetType.TRUST, totalTrusts);
        updateAssetEntity(member, AssetType.SECURITIES, totalSecurities);
        updateAssetEntity(member, AssetType.VIRTUAL_ASSET, totalVirtualAssets);

        connectCars(member);
        connectCash(member);
        connectPensions(member);
        connectRealEstates(member);
    }

    private BigDecimal connectAndCalculateDeposits(MemberEntity member) {
        List<DepositEntity> deposits = depositRepository.findByAsset_Member(member);
        return deposits.stream()
                .peek(deposit -> {
                    deposit.setConnected(true);
                    depositRepository.save(deposit);
                })
                .map(DepositEntity::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal connectAndCalculateFunds(MemberEntity member) {
        List<FundEntity> funds = fundRepository.findByAsset_Member(member);
        return funds.stream()
                .peek(fund -> {
                    fund.setConnected(true);
                    fundRepository.save(fund);
                })
                .map(FundEntity::getFundAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal connectAndCalculateTrusts(MemberEntity member) {
        List<TrustEntity> trusts = trustRepository.findByAsset_Member(member);
        return trusts.stream()
                .peek(trust -> {
                    trust.setConnected(true);
                    trustRepository.save(trust);
                })
                .map(TrustEntity::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal connectAndCalculateSecuritiesAccounts(MemberEntity member) {
        List<SecuritiesAccountEntity> accounts = securitiesAccountRepository.findByAsset_Member(member);
        return accounts.stream()
                .peek(account -> {
                    account.setConnected(true);
                    securitiesAccountRepository.save(account);
                })
                .map(account -> account.getDeposit().add(account.getPrincipal()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal connectAndCalculateVirtualAssets(MemberEntity member) {
        List<VirtualAsset> assets = virtualAssetRepository.findByAsset_Member(member);
        return assets.stream()
                .peek(asset -> {
                    asset.setConnected(true);
                    virtualAssetRepository.save(asset);
                })
                .map(asset -> asset.getCurrentPrice().multiply(asset.getQuantity()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void connectCars(MemberEntity member) {
        List<CarEntity> cars = carRepository.findByAsset_Member(member);
        cars.forEach(car -> {
            car.setConnected(true);
            carRepository.save(car);
        });
    }

    private void connectCash(MemberEntity member) {
        CashEntity cash = cashRepository.findByAsset_Member_MemberId(member.getMemberId())
                .orElseThrow(() -> new AssetHandler(ErrorStatus.CASH_NOT_FOUND));
        cash.setConnected(true);
        cashRepository.save(cash);
    }

    private void connectPensions(MemberEntity member) {
        List<PensionEntity> pensions = pensionRepository.findByAsset_Member(member);
        pensions.forEach(pension -> {
            pension.setConnected(true);
            pensionRepository.save(pension);
        });
    }

    private void connectRealEstates(MemberEntity member) {
        List<RealEstateEntity> realEstates = realEstateRepository.findByAsset_Member(member);
        realEstates.forEach(realEstate -> {
            realEstate.setConnected(true);
            realEstateRepository.save(realEstate);
        });
    }

    private void updateAssetEntity(MemberEntity member, AssetType assetType, BigDecimal totalAmount) {
        AssetEntity assetEntity = assetRepository.findByMemberAndAssetType(member, assetType)
                .orElseGet(() -> AssetEntity.builder()
                        .member(member)
                        .assetType(assetType)
                        .amount(0L)
                        .build());

        assetEntity.setAmount(totalAmount.longValue());
        assetRepository.save(assetEntity);
    }
}
