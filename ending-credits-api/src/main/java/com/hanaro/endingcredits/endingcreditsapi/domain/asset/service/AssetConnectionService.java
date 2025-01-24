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
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.enums.AssetType;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.enums.CurrencyCodeType;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.repository.AssetRepository;
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
import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.exception.handler.MemberHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
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
    private final AssetRepository assetRepository;

    private static final BigDecimal EXCHANGE_RATE = BigDecimal.valueOf(1450); // USD -> KRW 환율


    /**
     * 모든 자산 연결
     */
    @Transactional
    public void connectAllAssets(List<String> bankNames, List<String> securitiesCompanyNames, List<String> exchangeNames, UUID memberId) {
        MemberEntity member = getMember(memberId);

        // 은행 자산 연결
        if(bankNames.size() > 0) {
            for (String bankName : bankNames) {
                BankEntity bank = bankRepository.findByBankName(bankName)
                        .orElseThrow(() -> new IllegalArgumentException("은행을 찾을 수 없습니다: " + bankName));
                connectDeposits(bank, member);
                connectFunds(bank, member);
                connectTrusts(bank, member);
            }

            BigDecimal totalDeposits = calculateTotalDepositsForBanks(member, bankNames);
            updateAssetEntity(member, AssetType.DEPOSIT, totalDeposits);

            BigDecimal totalFunds = calculateTotalFundsForBanks(member, bankNames);
            updateAssetEntity(member, AssetType.FUND, totalFunds);

            BigDecimal totalTrusts = calculateTotalTrustsForBanks(member, bankNames);
            updateAssetEntity(member, AssetType.TRUST, totalTrusts);
        }

        // 증권 자산 연결
        if(securitiesCompanyNames.size() > 0) {
            for (String securitiesCompanyName : securitiesCompanyNames) {
                SecuritiesCompanyEntity company = securitiesCompanyRepository.findBySecuritiesCompanyName(securitiesCompanyName)
                        .orElseThrow(() -> new IllegalArgumentException("증권회사를 찾을 수 없습니다: " + securitiesCompanyName));
                connectSecuritiesAccounts(company, member);
            }

            BigDecimal totalSecurities = calculateTotalSecuritiesForCompanies(member, securitiesCompanyNames);
            updateAssetEntity(member, AssetType.SECURITIES, totalSecurities);
        }

        // 가상자산 연결
        if(exchangeNames.size() > 0) {
            for (String exchangeName : exchangeNames) {
                ExchangeEntity exchange = exchangeRepository.findByExchangeName(exchangeName)
                        .orElseThrow(() -> new IllegalArgumentException("거래소를 찾을 수 없습니다: " + exchangeName));
                connectVirtualAssets(exchange, member);
            }

            BigDecimal totalVirtualAssets = calculateTotalVirtualAssetsForExchanges(member, exchangeNames);
            updateAssetEntity(member, AssetType.VIRTUAL_ASSET, totalVirtualAssets);
        }

        // 기타 자산 연결
        connectCars(member);
        connectCash(member);
        connectPensions(member);
        connectRealEstates(member);
    }

    // Private Helper Methods (기존 메서드 사용)

    private BigDecimal calculateTotalVirtualAssetsForExchanges(MemberEntity member, List<String> exchangeNames) {
        BigDecimal totalVirtualAssets = BigDecimal.ZERO;

        for (String exchangeName : exchangeNames) {
            // 거래소 엔티티 조회
            ExchangeEntity exchange = exchangeRepository.findByExchangeName(exchangeName)
                    .orElseThrow(() -> new IllegalArgumentException("거래소를 찾을 수 없습니다: " + exchangeName));

            // 해당 거래소의 가상자산 총액 계산
            BigDecimal virtualAssets = virtualAssetRepository.findByExchangeAndAsset_Member(exchange, member)
                    .stream()
                    .map(asset -> calculateVirtualAssetTotal(asset)) // 각 자산의 총액 계산
                    .reduce(BigDecimal.ZERO, BigDecimal::add); // 합산

            // 전체 가상자산 총액에 추가
            totalVirtualAssets = totalVirtualAssets.add(virtualAssets);
        }

        return totalVirtualAssets;
    }

    // 개별 가상자산의 총액 계산
    private BigDecimal calculateVirtualAssetTotal(VirtualAsset asset) {
        // 보유량 × 현재 가격 (통화 코드를 기준으로 적절한 환율 적용)
        BigDecimal totalValue = asset.getCurrentPrice().multiply(asset.getQuantity());

        if (asset.getCurrencyCode() == CurrencyCodeType.USD) {
            // USD -> KRW 변환
            totalValue = totalValue.multiply(EXCHANGE_RATE);
        }

        return totalValue;
    }


    private BigDecimal calculateTotalSecuritiesForCompanies(MemberEntity member, List<String> securitiesCompanyNames) {
        BigDecimal totalSecurities = BigDecimal.ZERO;

        for (String companyName : securitiesCompanyNames) {
            // 증권회사 엔티티 조회
            SecuritiesCompanyEntity company = securitiesCompanyRepository.findBySecuritiesCompanyName(companyName)
                    .orElseThrow(() -> new IllegalArgumentException("증권회사를 찾을 수 없습니다: " + companyName));

            // 해당 증권회사의 증권 계좌에서 총액 계산
            BigDecimal securities = securitiesAccountRepository.findBySecuritiesCompanyAndAsset_Member(company, member)
                    .stream()
                    .map(account -> calculateAccountTotal(account)) // 각 계좌의 총액 계산
                    .reduce(BigDecimal.ZERO, BigDecimal::add); // 합산

            // 전체 증권 총액에 추가
            totalSecurities = totalSecurities.add(securities);
        }

        return totalSecurities;
    }

    // 개별 계좌의 총액 계산
    private BigDecimal calculateAccountTotal(SecuritiesAccountEntity account) {
        // 예수금 + 원금 (통화 코드를 기준으로 적절한 환율 적용)
        BigDecimal deposit = account.getDeposit();
        BigDecimal principal = account.getPrincipal();

        if (account.getCurrencyCode() == CurrencyCodeType.USD) {
            // USD -> KRW 변환
            deposit = deposit.multiply(EXCHANGE_RATE);
            principal = principal.multiply(EXCHANGE_RATE);
        }

        return deposit.add(principal); // 총액 반환
    }


    private BigDecimal calculateTotalTrustsForBanks(MemberEntity member, List<String> bankNames) {
        BigDecimal totalTrusts = BigDecimal.ZERO;

        for (String bankName : bankNames) {
            BankEntity bank = bankRepository.findByBankName(bankName)
                    .orElseThrow(() -> new IllegalArgumentException("은행을 찾을 수 없습니다: " + bankName));

            // 해당 은행의 신탁 총액 계산
            BigDecimal trusts = trustRepository.findByBankAndAsset_Member(bank, member)
                    .stream()
                    .map(TrustEntity::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            totalTrusts = totalTrusts.add(trusts);
        }

        return totalTrusts;
    }

    private BigDecimal calculateTotalDepositsForBanks(MemberEntity member, List<String> bankNames) {
        BigDecimal totalDeposits = BigDecimal.ZERO;

        for (String bankName : bankNames) {
            BankEntity bank = bankRepository.findByBankName(bankName)
                    .orElseThrow(() -> new IllegalArgumentException("은행을 찾을 수 없습니다: " + bankName));

            // 해당 은행의 예금 총액 계산
            BigDecimal deposits = depositRepository.findByBankAndAsset_Member(bank, member)
                    .stream()
                    .map(DepositEntity::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            totalDeposits = totalDeposits.add(deposits);
        }

        return totalDeposits;
    }

    private BigDecimal calculateTotalFundsForBanks(MemberEntity member, List<String> bankNames) {
        BigDecimal totalFunds = BigDecimal.ZERO;

        for (String bankName : bankNames) {
            BankEntity bank = bankRepository.findByBankName(bankName)
                    .orElseThrow(() -> new IllegalArgumentException("은행을 찾을 수 없습니다: " + bankName));

            // 해당 은행의 펀드 총액 계산
            BigDecimal funds = fundRepository.findByBankAndAsset_Member(bank, member)
                    .stream()
                    .map(FundEntity::getFundAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            totalFunds = totalFunds.add(funds);
        }

        return totalFunds;
    }

    private void updateAssetEntity(MemberEntity member, AssetType assetType, BigDecimal totalAmount) {
        // AssetEntity 조회 또는 생성
        AssetEntity assetEntity = assetRepository.findByMemberAndAssetType(member, assetType)
                .orElseGet(() -> AssetEntity.builder()
                        .member(member)
                        .assetType(assetType)
                        .amount(0L)
                        .build());

        // 총액 업데이트
        assetEntity.setAmount(totalAmount.longValue());
        assetRepository.save(assetEntity);
    }

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
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
    }
}

