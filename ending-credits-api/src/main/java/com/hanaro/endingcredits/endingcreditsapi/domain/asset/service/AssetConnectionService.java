package com.hanaro.endingcredits.endingcreditsapi.domain.asset.service;

import com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities.AssetEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities.bank.BankEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities.bank.DepositEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities.bank.FundEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities.bank.TrustEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities.etc.CarEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities.etc.CashEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities.etc.PensionEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities.etc.RealEstateEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities.securities.SecuritiesAccountEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities.securities.SecuritiesCompanyEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities.virtual.ExchangeEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities.virtual.VirtualAsset;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.repository.bank.BankRepository;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.repository.bank.DepositRepository;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.repository.bank.FundRepository;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.repository.bank.TrustRepository;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.repository.etc.CarRepository;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.repository.etc.CashRepository;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.repository.etc.PensionRepository;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.repository.etc.RealEstateRepository;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.repository.securities.SecuritiesAccountRepository;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.repository.securities.SecuritiesCompanyRepository;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.repository.virtual.ExchangeRepository;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.repository.virtual.VirtualAssetRepository;
import com.hanaro.endingcredits.endingcreditsapi.domain.member.entities.MemberEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.member.repository.MemberRepository;
import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.code.status.ErrorStatus;
import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.exception.handler.AssetHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AssetConnectionService {

    // 기존의 Repositories 주입
    private final MemberRepository memberRepository;
    private final BankRepository bankRepository;
    private final DepositRepository depositRepository;
    private final FundRepository fundRepository;
    private final TrustRepository trustRepository;
    private final SecuritiesCompanyRepository securitiesCompanyRepository;
    private final SecuritiesAccountRepository securitiesAccountRepository;
    private final ExchangeRepository exchangeRepository;
    private final VirtualAssetRepository virtualAssetRepository;
    private final CarRepository carRepository;
    private final CashRepository cashRepository;
    private final PensionRepository pensionRepository;
    private final RealEstateRepository realEstateRepository;

    /**
     * 모든 자산 연결
     */
    @Transactional
    public void connectAllAssets(List<String> bankNames, List<String> securitiesCompanyNames, List<String> exchangeNames, UUID memberId) {
        MemberEntity member = getMember(memberId);

        // 은행 자산 연결
        for (String bankName : bankNames) {
            BankEntity bank = bankRepository.findByBankName(bankName)
                    .orElseThrow(() -> new IllegalArgumentException("은행을 찾을 수 없습니다: " + bankName));
            connectDeposits(bank, member);
            connectFunds(bank, member);
            connectTrusts(bank, member);
        }

        // 증권 자산 연결
        for (String securitiesCompanyName : securitiesCompanyNames) {
            SecuritiesCompanyEntity company = securitiesCompanyRepository.findBySecuritiesCompanyName(securitiesCompanyName)
                    .orElseThrow(() -> new IllegalArgumentException("증권회사를 찾을 수 없습니다: " + securitiesCompanyName));
            connectSecuritiesAccounts(company, member);
        }

        // 가상자산 연결
        for (String exchangeName : exchangeNames) {
            ExchangeEntity exchange = exchangeRepository.findByExchangeName(exchangeName)
                    .orElseThrow(() -> new IllegalArgumentException("거래소를 찾을 수 없습니다: " + exchangeName));
            connectVirtualAssets(exchange, member);
        }

        // 기타 자산 연결
        connectCars(member);
        connectCash(member);
        connectPensions(member);
        connectRealEstates(member);
    }

    // Private Helper Methods (기존 메서드 사용)

    private void connectDeposits(BankEntity bank, MemberEntity member) {
        List<DepositEntity> deposits = depositRepository.findByBankAndAsset_Member(bank, member);
        deposits.forEach(deposit -> {
            deposit.setConnected(true);
            depositRepository.save(deposit);
        });
    }

    private void connectFunds(BankEntity bank, MemberEntity member) {
        List<FundEntity> funds = fundRepository.findByBankAndAsset_Member(bank, member);
        funds.forEach(fund -> {
            fund.setConnected(true);
            fundRepository.save(fund);
        });
    }

    private void connectTrusts(BankEntity bank, MemberEntity member) {
        List<TrustEntity> trusts = trustRepository.findByBankAndAsset_Member(bank, member);
        trusts.forEach(trust -> {
            trust.setConnected(true);
            trustRepository.save(trust);
        });
    }

    private void connectSecuritiesAccounts(SecuritiesCompanyEntity company, MemberEntity member) {
        List<SecuritiesAccountEntity> accounts = securitiesAccountRepository.findBySecuritiesCompanyAndAsset_Member(company, member);
        accounts.forEach(account -> {
            account.setConnected(true);
            securitiesAccountRepository.save(account);
        });
    }

    private void connectVirtualAssets(ExchangeEntity exchange, MemberEntity member) {
        List<VirtualAsset> assets = virtualAssetRepository.findByExchangeAndAsset_Member(exchange, member);
        assets.forEach(asset -> {
            asset.setConnected(true);
            virtualAssetRepository.save(asset);
        });
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

    private MemberEntity getMember(UUID memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다. ID: " + memberId));
    }
}

