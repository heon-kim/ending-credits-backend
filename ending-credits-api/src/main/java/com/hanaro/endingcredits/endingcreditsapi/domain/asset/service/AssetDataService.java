package com.hanaro.endingcredits.endingcreditsapi.domain.asset.service;

import com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities.AssetEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities.LoanEntity;
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
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.enums.PensionType;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.enums.RealEstateType;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.repository.AssetRepository;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.repository.LoanRepository;
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
import com.hanaro.endingcredits.endingcreditsapi.domain.member.dto.LoginType;
import com.hanaro.endingcredits.endingcreditsapi.domain.member.entities.MemberEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AssetDataService {

    private final AssetRepository assetRepository;
    private final CarRepository carRepository;
    private final TrustRepository trustRepository;
    private final FundRepository fundRepository;
    private final DepositRepository depositRepository;
    private final LoanRepository loanRepository;
    private final CashRepository cashRepository;
    private final RealEstateRepository realEstateRepository;
    private final PensionRepository pensionRepository;
    private final VirtualAssetRepository virtualAssetRepository;
    private final SecuritiesAccountRepository securitiesAccountRepository;
    private final BankRepository bankRepository;
    private final ExchangeRepository exchangeRepository;
    private final SecuritiesCompanyRepository securitiesCompanyRepository;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    private static final BigDecimal EXCHANGE_RATE = BigDecimal.valueOf(1450); // USD -> KRW 환율


    @Transactional
    public void generateMockData() {

        MemberEntity member = memberRepository.save(
                MemberEntity.builder()
                        .identifier("rladlsdud789")
                        .password(passwordEncoder.encode("12345"))
                        .simplePassword("1234")
                        .email("rladlsdud789@naver.com")
                        .loginType(LoginType.NORMAL)
                        .birthDate(LocalDate.of(1999, 6, 28))
                        .phoneNumber("010-9057-2190")
                        .address("서울시 구로구 신도림로 32")
                        .name("김인영")
                        .isActive(true)
                        .build()
        );
        // 은행 생성
        List<BankEntity> banks = createBanks();
        // 저축 은행 생성
        List<BankEntity> savingsBanks = createSavingsBanks();
        // 거래소 생성
        List<ExchangeEntity> exchanges = createExchanges();
        // 증권사 생성
        List<SecuritiesCompanyEntity> securitiesCompanies = createSecuritiesCompanies();

        // 자산 데이터 생성
        createMockAssets(member, banks, savingsBanks, exchanges, securitiesCompanies);
    }

    private List<BankEntity> createBanks() {
        String[] bankNames = {"KB국민은행", "카카오뱅크", "신한은행", "NH농협은행", "지역농협",
                "하나은행", "우리은행", "IBK기업은행", "케이뱅크", "새마을금고",
                "우체국", "신협", "SC제일은행", "iM뱅크", "BNK부산은행",
                "BNK경남은행", "광주은행", "전북은행", "수협은행", "수협중앙회",
                "씨티은행", "제주은행", "KDB산업은행", "산림조합중앙회",
                "한국수출입은행", "한국농수한식품유통공사", "한국장학재단",
                "한국주택금융공사", "신용회복위원회", "서민금융진흥원"};
        return createBankEntities(bankNames, false);
    }

    private List<BankEntity> createSavingsBanks() {
        String[] savingsBankNames = {"SBI저축은행", "OK저축은행", "웰컴저축은행", "신한저축은행",
                "KB저축은행", "페퍼저축은행", "다올저축은행", "애큐온저축은행",
                "하나저축은행", "NH저축은행", "한국투자저축은행", "상상인저축은행",
                "IBK저축은행", "우리금융저축은행", "BNK저축은행", "고려저축은행",
                "국제저축은행", "금화저축은행", "남양저축은행", "대명저축은행"};
        return createBankEntities(savingsBankNames, true);
    }

    private List<BankEntity> createBankEntities(String[] names, boolean isSavingsBank) {
        List<BankEntity> banks = new ArrayList<>();
        for (String name : names) {
            BankEntity bank = BankEntity.builder()
                    .bankName(name)
                    .isSavingsBank(isSavingsBank)
                    .build();
            banks.add(bankRepository.save(bank));
        }
        return banks;
    }

    private List<ExchangeEntity> createExchanges() {
        String[] exchangeNames = {"업비트", "빗썸", "코인원", "코빗", "고팍스"};
        List<ExchangeEntity> exchanges = new ArrayList<>();
        for (String name : exchangeNames) {
            ExchangeEntity exchange = ExchangeEntity.builder()
                    .exchangeName(name)
                    .build();
            exchanges.add(exchangeRepository.save(exchange));
        }
        return exchanges;
    }

    private List<SecuritiesCompanyEntity> createSecuritiesCompanies() {
        String[] securitiesCompanyNames = {"한국투자증권", "키움증권", "미래에셋증권", "신한투자증권",
                "NH투자증권", "KB증권", "삼성증권", "카카오페이증권", "하나증권",
                "대신증권", "유안타증권", "한화투자증권", "DB금융투자",
                "유진투자증권", "SK증권", "현대차증권", "IBK투자증권",
                "하이투자증권", "신영증권", "LS증권", "우리종합금융",
                "한국포스증권", "메리츠증권", "교보증권", "다올투자증권",
                "코리아에셋투자증권", "BNK투자증권", "케이프투자증권",
                "한국증권금융", "부국증권"};
        List<SecuritiesCompanyEntity> companies = new ArrayList<>();
        for (String name : securitiesCompanyNames) {
            SecuritiesCompanyEntity company = SecuritiesCompanyEntity.builder()
                    .securitiesCompanyName(name)
                    .build();
            companies.add(securitiesCompanyRepository.save(company));
        }
        return companies;
    }

    private void createMockAssets(MemberEntity member, List<BankEntity> banks, List<BankEntity> savingsBanks, List<ExchangeEntity> exchanges, List<SecuritiesCompanyEntity> securitiesCompanies) {

        AssetEntity depositAsset = createAsset(member, AssetType.DEPOSIT, 10L);
        createMultipleDepositsForAsset(depositAsset, banks);

        AssetEntity trustAsset = createAsset(member, AssetType.TRUST, 10L);
        createMultipleTrustsForAsset(trustAsset, savingsBanks);

        AssetEntity fundAsset = createAsset(member, AssetType.FUND, 10L);
        createMultipleFundsForAsset(fundAsset, banks);

        AssetEntity virtualAsset = createAsset(member, AssetType.VIRTUAL_ASSET, 20L);
        createMultipleVirtualAssetsForAsset(virtualAsset, exchanges);

        AssetEntity securitiesAsset = createAsset(member, AssetType.SECURITIES, 10L);
        createMultipleSecuritiesAssetsForAsset(securitiesAsset, securitiesCompanies);

        AssetEntity carAsset = createAsset(member, AssetType.CAR, 10L);
        createMultipleCarsForAsset(carAsset);

        AssetEntity realEstateAsset = createAsset(member, AssetType.REAL_ESTATE, 20L);
        createMultipleRealEstatesForAsset(realEstateAsset);

        AssetEntity pensionAsset = createAsset(member, AssetType.PENSION, 20L);
        createMultiplePensionsForAsset(pensionAsset);

        createCash(member, BigDecimal.valueOf(500000));
    }

    private AssetEntity createAsset(MemberEntity member, AssetType type, Long amount) {
        AssetEntity asset = AssetEntity.builder()
                .assetType(type)
                .amount(amount)
                .member(member)
                .build();
        return assetRepository.save(asset);
    }

    private void createMultiplePensionsForAsset(AssetEntity asset) {
        long totalExpectedAmount = 0;

        // 연금 타입 배열
        PensionType[] pensionTypes = PensionType.values();

        for (int i = 0; i < 3; i++) {
            // 월 지급액 및 지급 기간 생성
            long monthlyPayment = 500_000L + (i * 50_000L); // 월 지급액
            int paymentDuration = 20 + i; // 지급 기간 (연 단위)

            // 총 예상 수령 금액 계산
            long expectedAmount = monthlyPayment * 12 * paymentDuration;
            totalExpectedAmount += expectedAmount; // 총 합계에 추가

            // 연금 데이터 생성
            PensionEntity pension = PensionEntity.builder()
                    .asset(asset)
                    .pensionType(pensionTypes[i]) // 순환하여 연금 타입 설정
                    .pensionAge(60 + i) // 연금 수령 시작 나이
                    .monthlyPayment(monthlyPayment) // 월 지급액
                    .paymentDuration(paymentDuration) // 지급 기간 (연 단위)
                    .totalExpectedAmount(expectedAmount) // 총 예상 수령 금액
                    .build();

            // 연금 데이터 저장
            pensionRepository.save(pension);
        }

        // AssetEntity의 총 자산 업데이트
        asset.setAmount(totalExpectedAmount);
        assetRepository.save(asset);
    }



    private void createMultipleRealEstatesForAsset(AssetEntity asset) {
        long totalAmount = 0;

        RealEstateType[] realEstateTypes = RealEstateType.values();

        for (int i = 0; i < 5; i++) {
            // 구매 가격 및 현재 시장 가격
            long purchasePrice = 100_000_000L + i * 10_000_000L; // 구매 가격
            long currentPrice = purchasePrice + (5_000_000L * i); // 현재 시장 가격

            // 총 금액 계산 (현재 가격 합산)
            totalAmount += currentPrice;

            RealEstateType realEstateType = realEstateTypes[i % realEstateTypes.length];

            // 부동산 데이터 생성
            RealEstateEntity realEstate = RealEstateEntity.builder()
                    .asset(asset)
                    .realEstateName("Real Estate " + i) // 부동산 이름
                    .address("123 Main St, City " + i) // 부동산 주소
                    .purchasePrice(purchasePrice) // 구매 가격
                    .currentPrice(currentPrice) // 현재 시장 가격
                    .realEstateType(realEstateType) // 부동산 타입
                    .build();

            // 부동산 데이터 저장
            realEstateRepository.save(realEstate);
        }

        // AssetEntity의 총 자산 업데이트
        asset.setAmount(totalAmount);
        assetRepository.save(asset);
    }


    private void createMultipleCarsForAsset(AssetEntity asset) {
        long totalAmount = 0;

        for (int i = 0; i < 5; i++) {
            // 자동차 구매 가격 및 현재 시장 가격
            long purchasePrice = 30_000_000L + i * 2_000_000L; // 구매 가격
            long currentMarketPrice = generateRandomMarketPrice(purchasePrice, 0.8, 1.2); // 현재 시장 가격

            // 총 금액 계산 (현재 시장 가격)
            totalAmount += currentMarketPrice;

            // 자동차 데이터 생성
            CarEntity car = CarEntity.builder()
                    .asset(asset)
                    .model("Car Model " + i) // 자동차 모델명
                    .carNumber("33루 867" + i) // 자동차 번호
                    .purchasePrice(purchasePrice) // 구매 가격
                    .currentMarketPrice(currentMarketPrice) // 현재 시장 가격
                    .mileage(10_000 + i * 1_000) // 주행 거리
                    .manufactureYear(2020 + i) // 제조 연도
                    .build();

            // 자동차 데이터 저장
            carRepository.save(car);
        }

        // AssetEntity의 총 자산 업데이트
        asset.setAmount(totalAmount);
        assetRepository.save(asset);
    }


    private void createMultipleSecuritiesAssetsForAsset(AssetEntity asset, List<SecuritiesCompanyEntity> securitiesCompanies) {
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (int i = 0; i < securitiesCompanies.size(); i++) {
            BigDecimal depositKRW = BigDecimal.valueOf(1_000_000 + i * 100_000); // 예수금 (KRW)
            BigDecimal principalKRW = BigDecimal.valueOf(5_000_000 + i * 500_000); // 원금 (KRW)

            BigDecimal depositUSD = depositKRW.divide(EXCHANGE_RATE, 2, BigDecimal.ROUND_HALF_UP); // 예수금 (USD)
            BigDecimal principalUSD = principalKRW.divide(EXCHANGE_RATE, 2, BigDecimal.ROUND_HALF_UP); // 원금 (USD)

            BigDecimal profitRatio = BigDecimal.valueOf(5 + i); // 수익률 예시

            // KRW 증권 계좌 생성
            SecuritiesAccountEntity accountKRW = SecuritiesAccountEntity.builder()
                    .asset(asset)
                    .securitiesCompany(securitiesCompanies.get(i))
                    .securitiesAccountNumber("KRW-1234-" + i)
                    .securitiesAccountName("Securities KRW " + i)
                    .deposit(depositKRW)
                    .principal(principalKRW)
                    .currencyCode(CurrencyCodeType.KRW)
                    .profitRatio(profitRatio)
                    .build();
            securitiesAccountRepository.save(accountKRW);

            // USD 증권 계좌 생성
            SecuritiesAccountEntity accountUSD = SecuritiesAccountEntity.builder()
                    .asset(asset)
                    .securitiesCompany(securitiesCompanies.get(i))
                    .securitiesAccountNumber("USD-5678-" + i)
                    .securitiesAccountName("Securities USD " + i)
                    .deposit(depositUSD)
                    .principal(principalUSD)
                    .currencyCode(CurrencyCodeType.USD)
                    .profitRatio(profitRatio)
                    .build();
            securitiesAccountRepository.save(accountUSD);

            // KRW 및 USD 금액을 합산하여 자산 총액 업데이트
//            totalAmount = totalAmount.add(depositKRW.add(principalKRW)); // KRW 금액 추가
//            totalAmount = totalAmount.add(depositUSD.add(principalUSD).multiply(EXCHANGE_RATE)); // USD 금액을 KRW로 변환 후 추가
        }

        // AssetEntity의 총 자산 업데이트
        asset.setAmount(totalAmount.longValue());
        assetRepository.save(asset);
    }

    private void createMultipleVirtualAssetsForAsset(AssetEntity asset, List<ExchangeEntity> exchanges) {
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (int i = 0; i < exchanges.size(); i++) {
            BigDecimal quantity = BigDecimal.valueOf(1.5 + i * 0.5); // 보유량
            BigDecimal purchasePriceKRW = BigDecimal.valueOf(1_000_000 + i * 200_000); // 구매 가격 (KRW)
            BigDecimal currentPriceKRW = BigDecimal.valueOf(1_200_000 + i * 300_000); // 현재 가격 (KRW)

            BigDecimal purchasePriceUSD = purchasePriceKRW.divide(EXCHANGE_RATE, 2, BigDecimal.ROUND_HALF_UP); // 구매 가격 (USD)
            BigDecimal currentPriceUSD = currentPriceKRW.divide(EXCHANGE_RATE, 2, BigDecimal.ROUND_HALF_UP); // 현재 가격 (USD)

            // KRW 가상 자산 생성
            VirtualAsset assetKRW = VirtualAsset.builder()
                    .asset(asset)
                    .exchange(exchanges.get(i))
                    .virtualAssetName("Crypto KRW " + i)
                    .quantity(quantity)
                    .purchasePrice(purchasePriceKRW)
                    .currentPrice(currentPriceKRW)
                    .profitRatio(calculateProfitRatio(purchasePriceKRW, currentPriceKRW))
                    .totalValue(quantity.multiply(currentPriceKRW)) // 총 가치 (KRW)
                    .currencyCode(CurrencyCodeType.KRW)
                    .build();
            virtualAssetRepository.save(assetKRW);

            // USD 가상 자산 생성
            VirtualAsset assetUSD = VirtualAsset.builder()
                    .asset(asset)
                    .exchange(exchanges.get(i))
                    .virtualAssetName("Crypto USD " + i)
                    .quantity(quantity)
                    .purchasePrice(purchasePriceUSD)
                    .currentPrice(currentPriceUSD)
                    .profitRatio(calculateProfitRatio(purchasePriceUSD, currentPriceUSD))
                    .totalValue(quantity.multiply(currentPriceUSD).multiply(EXCHANGE_RATE)) // 총 가치 (KRW 환산)
                    .currencyCode(CurrencyCodeType.USD)
                    .build();
            virtualAssetRepository.save(assetUSD);

            // KRW 및 USD 금액을 합산하여 자산 총액 계산
//            totalAmount = totalAmount.add(quantity.multiply(currentPriceKRW)); // KRW 총 가치 추가
//            totalAmount = totalAmount.add(quantity.multiply(currentPriceUSD).multiply(EXCHANGE_RATE)); // USD 총 가치를 KRW로 변환 후 추가
        }

        // AssetEntity의 총 자산 업데이트
        asset.setAmount(totalAmount.longValue());
        assetRepository.save(asset);
    }

    private void createMultipleFundsForAsset(AssetEntity asset, List<BankEntity> banks) {
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (int i = 0; i < banks.size(); i++) {
            BigDecimal principalKRW = BigDecimal.valueOf(2_000_000 + i * 200_000); // 투자 원금 (KRW)
            BigDecimal principalUSD = principalKRW.divide(EXCHANGE_RATE, 2, BigDecimal.ROUND_HALF_UP); // 투자 원금 (USD)

            BigDecimal fundAmountKRW = principalKRW.multiply(BigDecimal.valueOf(1.1)); // 수익 포함 (KRW)
            BigDecimal fundAmountUSD = principalUSD.multiply(BigDecimal.valueOf(1.1)); // 수익 포함 (USD)

            // KRW 펀드 생성
            FundEntity fundKRW = FundEntity.builder()
                    .bank(banks.get(i))
                    .asset(asset)
                    .accountName("Fund KRW " + i)
                    .accountNumber("FUND-123-KRW-" + i)
                    .investmentPrincipal(principalKRW)
                    .fundAmount(fundAmountKRW) // 수익 포함 금액 (KRW)
                    .profitRatio(calculateProfitRatio(principalKRW, fundAmountKRW)) // 수익률
                    .currencyCode(CurrencyCodeType.KRW)
                    .build();
            fundRepository.save(fundKRW);

            // USD 펀드 생성
//            FundEntity fundUSD = FundEntity.builder()
//                    .bank(banks.get(i))
//                    .asset(asset)
//                    .accountName("Fund USD " + i)
//                    .accountNumber("FUND-123-USD-" + i)
//                    .investmentPrincipal(principalUSD)
//                    .fundAmount(fundAmountUSD.multiply(EXCHANGE_RATE)) // 수익 포함 금액을 KRW로 환산
//                    .profitRatio(calculateProfitRatio(principalUSD, fundAmountUSD)) // 수익률
//                    .currencyCode(CurrencyCodeType.USD)
//                    .build();
//            fundRepository.save(fundUSD);

            // 총 금액 계산 (KRW 및 USD 환산 후 합산)
//            totalAmount = totalAmount.add(fundAmountKRW);
//            totalAmount = totalAmount.add(fundAmountUSD.multiply(EXCHANGE_RATE));
        }

        // AssetEntity의 총 자산 업데이트
        asset.setAmount(totalAmount.longValue());
        assetRepository.save(asset);
    }

    private void createMultipleTrustsForAsset(AssetEntity asset, List<BankEntity> banks) {
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (int i = 0; i < banks.size(); i++) {
            BigDecimal amountKRW = BigDecimal.valueOf(1_500_000 + i * 100_000); // 신탁 금액 (KRW)
            BigDecimal amountUSD = amountKRW.divide(EXCHANGE_RATE, 2, BigDecimal.ROUND_HALF_UP); // 신탁 금액 (USD)

            // KRW 신탁 생성
            TrustEntity trustKRW = TrustEntity.builder()
                    .bank(banks.get(i))
                    .asset(asset)
                    .accountName("Trust KRW " + i)
                    .accountNumber("TRUST-123-KRW-" + i)
                    .amount(amountKRW)
                    .currencyCode(CurrencyCodeType.KRW)
                    .build();
            trustRepository.save(trustKRW);

            // USD 신탁 생성
            TrustEntity trustUSD = TrustEntity.builder()
                    .bank(banks.get(i))
                    .asset(asset)
                    .accountName("Trust USD " + i)
                    .accountNumber("TRUST-123-USD-" + i)
                    .amount(amountUSD)
                    .currencyCode(CurrencyCodeType.USD)
                    .build();
            trustRepository.save(trustUSD);

            // 총 금액 계산 (KRW 및 USD 환산 후 합산)
//            totalAmount = totalAmount.add(amountKRW);
//            totalAmount = totalAmount.add(amountUSD.multiply(EXCHANGE_RATE));
        }

        // AssetEntity의 총 자산 업데이트
        asset.setAmount(totalAmount.longValue());
        assetRepository.save(asset);
    }

    private void createMultipleDepositsForAsset(AssetEntity asset, List<BankEntity> banks) {
        BigDecimal totalAmount = BigDecimal.ZERO;
        DepositEntity deposit = null;

        for (int i = 0; i < banks.size(); i++) {
            BigDecimal amountKRW = BigDecimal.valueOf(2_000_000 + i * 200_000); // 예금 금액 (KRW)
            BigDecimal amountUSD = amountKRW.divide(EXCHANGE_RATE, 2, BigDecimal.ROUND_HALF_UP); // 예금 금액 (USD)

            // KRW 예금 생성
            DepositEntity depositKRW = DepositEntity.builder()
                    .bank(banks.get(i))
                    .asset(asset)
                    .accountName("Deposit KRW " + i)
                    .accountNumber("DEPOSIT-KRW-" + i)
                    .amount(amountKRW)
                    .currencyCode(CurrencyCodeType.KRW)
                    .build();
            deposit = depositRepository.save(depositKRW);

//            // USD 예금 생성
//            DepositEntity depositUSD = DepositEntity.builder()
//                    .bank(banks.get(i))
//                    .asset(asset)
//                    .accountName("Deposit USD " + i)
//                    .accountNumber("DEPOSIT-USD-" + i)
//                    .amount(amountUSD)
//                    .currencyCode(CurrencyCodeType.USD)
//                    .build();
//
//            deposit = depositRepository.save(depositUSD);
            // 총 금액 계산 (KRW 및 USD 환산 후 합산)
//            totalAmount = totalAmount.add(amountKRW);
//            totalAmount = totalAmount.add(amountUSD.multiply(EXCHANGE_RATE));
        }

        createLoan(deposit);
        // AssetEntity의 총 자산 업데이트
        asset.setAmount(totalAmount.longValue());
        assetRepository.save(asset);
    }

    private void createCash(MemberEntity member, BigDecimal amount) {
        AssetEntity asset = createAsset(member, AssetType.CASH, amount.longValue());
        CashEntity cash = CashEntity.builder()
                .asset(asset)
                .amount(amount)
                .build();
        cashRepository.save(cash);
    }

    private DepositEntity createSingleDeposit(MemberEntity member, BankEntity bank, String accountName, String accountNumber, BigDecimal amount) {
        AssetEntity asset = createAsset(member, AssetType.DEPOSIT, amount.longValue());
        DepositEntity deposit = DepositEntity.builder()
                .bank(bank)
                .asset(asset)
                .accountName(accountName)
                .accountNumber(accountNumber)
                .amount(amount)
                .currencyCode(CurrencyCodeType.KRW)
                .build();
        return depositRepository.save(deposit);
    }


    private void createLoan(DepositEntity deposit) {
        for(int i = 0; i < 3; i++) {
            BigDecimal totalAmount = generateRandomAmount(100000, 1000000);              // 100,000 ~ 1,000,000 사이의 금액
            BigDecimal loanAmount = generateRandomAmount(10000, totalAmount.intValue()); // 10,000 ~ totalAmount 사이의 금액
            LocalDate expiryDate = generateRandomExpiryDate();                           // 오늘 이후의 랜덤 날짜

        LoanEntity loan = LoanEntity.builder()
                .deposit(deposit)
                .totalAmount(totalAmount)
                .loanAmount(loanAmount)
                .expiryDate(expiryDate)
                .build();

            loanRepository.save(loan);
        }
    }

    private BigDecimal calculateProfitRatio(BigDecimal purchasePrice, BigDecimal currentPrice) {
        if (purchasePrice.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return currentPrice.subtract(purchasePrice)
                .divide(purchasePrice, 2, BigDecimal.ROUND_HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }

    private BigDecimal generateRandomAmount(int min, int max) {
        Random random = new Random();

        int randomValue = random.nextInt(max - min + 1) + min;
        return BigDecimal.valueOf(randomValue);
    }

    private LocalDate generateRandomExpiryDate() {
        Random random = new Random();

        int daysToAdd = random.nextInt(365 * 5) + 1; // 1 ~ 5년 이내의 랜덤 날짜
        return LocalDate.now().plusDays(daysToAdd);
    }

    private long generateRandomMarketPrice(long purchasePrice, double minRate, double maxRate) {
        double randomRate = minRate + Math.random() * (maxRate - minRate);
        return Math.round(purchasePrice * randomRate);
    }
}

