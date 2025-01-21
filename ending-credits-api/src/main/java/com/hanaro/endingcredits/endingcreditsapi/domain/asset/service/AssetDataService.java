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
import io.jsonwebtoken.security.Password;
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
        String[] bankNames = {"Hana Bank", "Shinhan Bank", "NH Bank", "Woori Bank", "SC Bank", "KB Bank", "IM Bank", "City Bank", "Kakao Bank"};
        return createBankEntities(bankNames, false);
    }

    private List<BankEntity> createSavingsBanks() {
        String[] savingsBankNames = {"DB Bank", "JT Bank", "Namyang Bank", "BNK Bank", "GuemHwa Bank", "DH Bank", "IBK Bank", "CK Bank", "Daebek Bank"};
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
        String[] exchangeNames = {"Upbit", "BitSum", "CoinOne", "Cobit", "GoPacks", "Pemex", "HuobiWeb", "FoblGate", "FlyBit"};
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
        String[] securitiesCompanyNames = {"KB Securities", "NH Securities", "SC Securities", "SK Securities", "Kyobo Securities", "GoldenBridge Securities", "Mirae Asset Securities", "Daesin Securities", "Kium Securities"};
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
        // 각 은행에 자산 연결
        for (int i = 0; i < banks.size(); i++) {
            createDeposit(member, banks.get(i), "Deposit Account " + i, "123-456-78" + i, BigDecimal.valueOf(1_000_000 + i * 100_000));
            // 신탁 : 짝수는 KRW, 홀수는 USD
            if (i % 2 == 0) {
                createTrust(
                        member,
                        banks.get(i),
                        "Trust Account " + i,
                        "123-456-78" + i,
                        BigDecimal.valueOf(1_000_000 + i * 100_000), // 금액 (KRW)
                        CurrencyCodeType.KRW
                );
            } else {
                createTrust(
                        member,
                        banks.get(i),
                        "Trust Account " + i,
                        "123-456-78" + i,
                        BigDecimal.valueOf(1_000 + i * 100), // 금액 (USD)
                        CurrencyCodeType.USD
                );
            }
            // 펀드: 짝수는 KRW, 홀수는 USD
            if (i % 2 == 0) {
                createFund(
                        member,
                        banks.get(i),
                        "Fund Account " + i,
                        "789-123-45" + i,
                        BigDecimal.valueOf(2_000_000 + i * 200_000), // 투자 원금 (KRW)
                        CurrencyCodeType.KRW
                );
            } else {
                createFund(
                        member,
                        banks.get(i),
                        "Fund Account " + i,
                        "789-123-45" + i,
                        BigDecimal.valueOf(2_000 + i * 200), // 투자 원금 (USD)
                        CurrencyCodeType.USD
                );
            }
            createLoan(createDeposit(member, banks.get(i), "Savings Account " + i, "890-123-45" + i, BigDecimal.valueOf(3000000 + i * 300000)));
        }



        // 각 거래소에 가상자산 연결
        for(int i = 0 ; i < exchanges.size(); i++){
            if(i % 2 == 0){
                //KRW 가상 자산
                createVirtualAsset(
                        member,
                        exchanges.get(i),
                        "Crypto Asset " + i,
                        BigDecimal.valueOf(1.5 + i * 0.5),
                        BigDecimal.valueOf(4500000 + i * 500000), // 구매 가격 (KRW)
                        BigDecimal.valueOf(5000000 + i * 500000), // 현재 가격 (KRW)
                        CurrencyCodeType.KRW
                );
            } else {
                // USD 가상 자산
                createVirtualAsset(
                        member,
                        exchanges.get(i),
                        "Crypto Asset " + i,
                        BigDecimal.valueOf(1.5 + i * 0.5),
                        BigDecimal.valueOf(3000 + i * 200), // 구매 가격 (USD)
                        BigDecimal.valueOf(3500 + i * 300), // 현재 가격 (USD)
                        CurrencyCodeType.USD
                );
            }
        }

        // 각 증권사에 증권 계좌 연결
        for (int i = 0; i < securitiesCompanies.size(); i++) {
            if (i % 2 == 0) {
                // KRW로 저장
                createSecuritiesAccount(
                        member,
                        securitiesCompanies.get(i),
                        "1234-5678-99" + i,
                        "Securities Account " + i,
                        BigDecimal.valueOf(1_000_000 + i * 200_000), // 예수금
                        BigDecimal.valueOf(5_000_000 + i * 300_000), // 원금 (KRW)
                        BigDecimal.valueOf(i * 2.5), // 수익률
                        CurrencyCodeType.KRW // 통화 코드
                );
            } else {
                // USD로 저장
                createSecuritiesAccount(
                        member,
                        securitiesCompanies.get(i),
                        "1234-5678-9912" + i,
                        "Securities Account " + i,
                        BigDecimal.valueOf(1_000_000 + i * 200_000), // 예수금 (KRW로 가정)
                        BigDecimal.valueOf(5_000 + i * 300), // 원금 (USD)
                        BigDecimal.valueOf(i * 2.5), // 수익률
                        CurrencyCodeType.USD // 통화 코드
                );
            }
        }

        // 기타 자산 생성
        for (int i = 0; i < 5; i++) {
            createCar(member, "아이오닉" + i, "33루 867" + i, 30000000L + i * 2000000, 15000 + i * 1000);
            createRealEstate(member, "Real Estate " + i, "123 Main St, City " + i, 100000000L + i * 5000000, 120000000L + i * 6000000);
            createPension(member, PensionType.NATIONAL, 60 + i, 500000L + i * 50000);
        }
        createCash(member, BigDecimal.valueOf(500000));
    }

    private void createSecuritiesAccount(MemberEntity member, SecuritiesCompanyEntity company, String securitiesAccountNumber, String securitiesAccountName, BigDecimal deposit, BigDecimal principal, BigDecimal profitRatio, CurrencyCodeType currencyCode) {
        // 환율 고정 값 (달러 -> 원화 변환)
        final BigDecimal EXCHANGE_RATE = BigDecimal.valueOf(1450);

        // amount 계산 (환율 적용)
        BigDecimal adjustedPrincipal = currencyCode == CurrencyCodeType.USD
                ? principal.multiply(EXCHANGE_RATE) // USD -> KRW 변환
                : principal;

        // 자산 생성
        AssetEntity asset = createAsset(member, AssetType.SECURITIES, adjustedPrincipal.longValue());

        // 증권 계좌 생성 및 저장
        SecuritiesAccountEntity account = SecuritiesAccountEntity.builder()
                .asset(asset)
                .securitiesCompany(company)
                .securitiesAccountNumber(securitiesAccountNumber)
                .securitiesAccountName(securitiesAccountName)
                .deposit(deposit) // 예수금은 원화로 가정
                .principal(principal) // 원금은 달러일 수 있으므로 원래 값을 저장
                .currencyCode(currencyCode) // 통화 코드 저장
                .profitRatio(profitRatio) // 수익률
                .build();
        securitiesAccountRepository.save(account);
    }



    private AssetEntity createAsset(MemberEntity member, AssetType type, Long amount) {
        AssetEntity asset = AssetEntity.builder()
                .assetType(type)
                .amount(amount)
                .member(member)
                .build();
        return assetRepository.save(asset);
    }

    private void createTrust(MemberEntity member, BankEntity bank, String accountName, String accountNumber, BigDecimal amount, CurrencyCodeType currencyCode) {
        // 환율 고정 값 (달러 -> 원화 변환)
        final BigDecimal EXCHANGE_RATE = BigDecimal.valueOf(1450);

        // 신탁 금액에 환율 적용 (USD -> KRW 변환)
        BigDecimal adjustedAmount = currencyCode == CurrencyCodeType.USD
                ? amount.multiply(EXCHANGE_RATE)
                : amount;

        // 자산 생성
        AssetEntity asset = createAsset(member, AssetType.TRUST, adjustedAmount.longValue());

        // 신탁 생성 및 저장
        TrustEntity trust = TrustEntity.builder()
                .bank(bank)
                .asset(asset)
                .accountName(accountName)
                .accountNumber(accountNumber)
                .amount(amount) // 입력 금액 (USD 또는 KRW)
                .currencyCode(currencyCode) // 통화 코드 저장
                .build();

        trustRepository.save(trust);
    }

    private void createFund(MemberEntity member, BankEntity bank, String accountName, String accountNumber, BigDecimal investmentPrincipal, CurrencyCodeType currencyCode) {
        // 환율 고정 값 (달러 -> 원화 변환)
        final BigDecimal EXCHANGE_RATE = BigDecimal.valueOf(1450);

        // 투자 원금에 환율 적용 (USD -> KRW 변환)
        BigDecimal adjustedInvestmentPrincipal = currencyCode == CurrencyCodeType.USD
                ? investmentPrincipal.multiply(EXCHANGE_RATE)
                : investmentPrincipal;

        // 자산 생성
        AssetEntity asset = createAsset(member, AssetType.FUND, adjustedInvestmentPrincipal.longValue());

        // 수익률과 펀드 금액 계산
        BigDecimal profitRatio = BigDecimal.valueOf(10.0);
        BigDecimal fundAmount = investmentPrincipal.multiply(BigDecimal.valueOf(1.1)); // 투자 원금의 10% 수익 가정

        // 펀드 생성 및 저장
        FundEntity fund = FundEntity.builder()
                .bank(bank)
                .asset(asset)
                .accountName(accountName)
                .accountNumber(accountNumber)
                .investmentPrincipal(investmentPrincipal) // 원금은 입력값 그대로 저장 (USD 또는 KRW)
                .fundAmount(currencyCode == CurrencyCodeType.USD
                        ? fundAmount.multiply(EXCHANGE_RATE) // USD -> KRW 변환
                        : fundAmount) // KRW는 그대로 저장
                .profitRatio(profitRatio) // 수익률
                .currencyCode(currencyCode) // 통화 코드 저장
                .build();
        fundRepository.save(fund);
    }

    private DepositEntity createDeposit(MemberEntity member, BankEntity bank, String accountName, String accountNumber, BigDecimal amount) {
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
        BigDecimal totalAmount = generateRandomAmount(100000, 1000000);              // 100,000 ~ 1,000,000 사이의 금액
        BigDecimal loanAmount = generateRandomAmount(10000, totalAmount.intValue()); // 10,000 ~ totalAmount 사이의 금액
        LocalDate expiryDate = generateRandomExpiryDate();                           // 오늘 이후의 랜덤 날짜

        LoanEntity loan = LoanEntity.builder()
                .depositId(deposit)
                .totalAmount(totalAmount)
                .loanAmount(loanAmount)
                .expiryDate(expiryDate)
                .build();

        loanRepository.save(loan);
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

    private void createVirtualAsset(MemberEntity member,
                                    ExchangeEntity exchange,
                                    String name,
                                    BigDecimal quantity,
                                    BigDecimal purchasePrice,
                                    BigDecimal currentPrice,
                                    CurrencyCodeType currencyCode) {
        // 환율 고정 값 (달러 -> 원화 변환)
        final BigDecimal EXCHANGE_RATE = BigDecimal.valueOf(1450);

        // 현재 가격과 구매 가격에 환율 적용 (USD -> KRW 변환)
        BigDecimal adjustedPurchasePrice = currencyCode == CurrencyCodeType.USD
                ? purchasePrice.multiply(EXCHANGE_RATE)
                : purchasePrice;
        BigDecimal adjustedCurrentPrice = currencyCode == CurrencyCodeType.USD
                ? currentPrice.multiply(EXCHANGE_RATE)
                : currentPrice;

        // 총 가치 계산
        BigDecimal totalValue = quantity.multiply(adjustedCurrentPrice);

        // 수익률 계산
        BigDecimal profitRatio = (adjustedPurchasePrice.compareTo(BigDecimal.ZERO) == 0)
                ? BigDecimal.ZERO
                : adjustedCurrentPrice.subtract(adjustedPurchasePrice)
                .divide(adjustedPurchasePrice, 2, BigDecimal.ROUND_HALF_UP)
                .multiply(BigDecimal.valueOf(100));

        // 자산 생성
        AssetEntity asset = createAsset(member, AssetType.VIRTUAL_ASSET, totalValue.longValue());

        // 가상 자산 생성 및 저장
        VirtualAsset virtualAsset = VirtualAsset.builder()
                .asset(asset)
                .exchange(exchange)
                .virtualAssetName(name)
                .quantity(quantity)
                .purchasePrice(adjustedPurchasePrice) // 조정된 구매 가격
                .currentPrice(adjustedCurrentPrice) // 조정된 현재 가격
                .profitRatio(profitRatio)
                .totalValue(totalValue) // 조정된 총 가치
                .currencyCode(currencyCode) // 통화 코드 저장
                .build();

        virtualAssetRepository.save(virtualAsset);
    }


    private void createCar(MemberEntity member, String model, String carNumber, Long purchasePrice, Integer mileage) {
        AssetEntity asset = createAsset(member, AssetType.CAR, purchasePrice);

        long currentMarketPrice = generateRandomMarketPrice(purchasePrice, 0.8, 1.2); // 구매가의 ±20% 범위
        int manufactureYear = generateRandomManufactureYear(2000, LocalDate.now().getYear() - 1); // 2000년 ~ 현재 이전 연도

        CarEntity car = CarEntity.builder()
                .asset(asset)
                .model(model)
                .carNumber(carNumber)
                .purchasePrice(purchasePrice)
                .currentMarketPrice(currentMarketPrice)
                .mileage(mileage)
                .manufactureYear(manufactureYear)
                .build();
        carRepository.save(car);
    }

    private long generateRandomMarketPrice(long purchasePrice, double minRate, double maxRate) {
        double randomRate = minRate + Math.random() * (maxRate - minRate);
        return Math.round(purchasePrice * randomRate);
    }

    private int generateRandomManufactureYear(int startYear, int endYear) {
        return startYear + (int) (Math.random() * (endYear - startYear + 1));
    }

    private void createCash(MemberEntity member, BigDecimal amount) {
        AssetEntity asset = createAsset(member, AssetType.CASH, amount.longValue());
        CashEntity cash = CashEntity.builder()
                .asset(asset)
                .amount(amount)
                .build();
        cashRepository.save(cash);
    }

    private void createRealEstate(MemberEntity member, String name, String address, Long purchasePrice, Long currentPrice) {
        AssetEntity asset = createAsset(member, AssetType.REAL_ESTATE, purchasePrice);
        RealEstateEntity realEstate = RealEstateEntity.builder()
                .asset(asset)
                .realEstateName(name)
                .address(address)
                .purchasePrice(purchasePrice)
                .currentPrice(currentPrice)
                .build();
        realEstateRepository.save(realEstate);
    }

    private void createPension(MemberEntity member, PensionType type, int pensionAge, Long amount) {
        AssetEntity asset = createAsset(member, AssetType.PENSION, amount);

        long monthlyPayment = generateRandomMonthlyAmount(100000, 1000000);   // 10만 원 ~ 100만 원 랜덤 금액
        int paymentDuration = generateRandomDuration(5, 30);                  // 5년 ~ 30년 랜덤 기간

        PensionEntity pension = PensionEntity.builder()
                .asset(asset)
                .pensionType(type)
                .pensionAge(pensionAge)
                .monthlyPayment(monthlyPayment)
                .paymentDuration(paymentDuration)
                .totalExpectedAmount(monthlyPayment * 12 * paymentDuration) // 총 예상 수령 금액 계산
                .build();
        pensionRepository.save(pension);
    }

    private long generateRandomMonthlyAmount(long min, long max) {
        return min + (long) (Math.random() * (max - min + 1));
    }

    private int generateRandomDuration(int min, int max) {
        return min + (int) (Math.random() * (max - min + 1));
    }
}

