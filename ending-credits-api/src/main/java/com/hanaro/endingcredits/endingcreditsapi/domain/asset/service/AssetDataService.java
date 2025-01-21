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
import lombok.RequiredArgsConstructor;
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

    @Transactional
    public void generateMockData() {

        MemberEntity member = memberRepository.save(
                MemberEntity.builder()
                        .identifier("rladlsdud789")
                        .password("12345")
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
            createTrust(member, banks.get(i), "Trust Account " + i, "123-456-78" + i, BigDecimal.valueOf(1000000 + i * 100000));
            createFund(member, banks.get(i), BigDecimal.valueOf(2000000 + i * 200000));
            createLoan(createDeposit(member, banks.get(i), "Savings Account " + i, "890-123-45" + i, BigDecimal.valueOf(3000000 + i * 300000)));
        }

        // 각 거래소에 가상자산 연결
        for (int i = 0; i < exchanges.size(); i++) {
            createVirtualAsset(member, exchanges.get(i), "Crypto Asset " + i, BigDecimal.valueOf(1.5 + i * 0.5), BigDecimal.valueOf(5000000 + i * 500000));
        }

        // 각 증권사에 증권 계좌 연결
        for (int i = 0; i < securitiesCompanies.size(); i++) {
            createSecuritiesAccount(member, securitiesCompanies.get(i), "Securities Account " + i, BigDecimal.valueOf(1000000 + i * 100000), BigDecimal.valueOf(5000000 + i * 200000), BigDecimal.valueOf(i + 0.5));
        }

        // 기타 자산 생성
        for (int i = 0; i < 5; i++) {
            createCar(member, "아이오닉" + i, "33루 867" + i, 30000000L + i * 2000000, 15000 + i * 1000);
            createRealEstate(member, "Real Estate " + i, "123 Main St, City " + i, 100000000L + i * 5000000, 120000000L + i * 6000000);
            createPension(member, PensionType.NATIONAL, 60 + i, 500000L + i * 50000);
        }
        createCash(member, BigDecimal.valueOf(500000 * 50000));
    }

    private void createSecuritiesAccount(MemberEntity member, SecuritiesCompanyEntity company, String securitiesAccountName, BigDecimal deposit, BigDecimal principal, BigDecimal profitRatio) {
        // 자산 생성
        AssetEntity asset = createAsset(member, AssetType.SECURITIES, principal.longValue());

        // 증권 계좌 생성 및 저장
        SecuritiesAccountEntity account = SecuritiesAccountEntity.builder()
                .asset(asset)
                .securitiesCompany(company)
                .securitiesAccountName(securitiesAccountName)
                .deposit(deposit)
                .principal(principal)
                .currencyCode(CurrencyCodeType.KRW)
                .profitRatio(profitRatio)
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

    private void createTrust(MemberEntity member, BankEntity bank, String accountName, String accountNumber, BigDecimal amount) {
        AssetEntity asset = createAsset(member, AssetType.TRUST, amount.longValue());
        TrustEntity trust = TrustEntity.builder()
                .bank(bank)
                .asset(asset)
                .accountName(accountName)
                .accountNumber(accountNumber)
                .amount(amount)
                .currencyCode(CurrencyCodeType.KRW)
                .build();
        trustRepository.save(trust);
    }

    private void createFund(MemberEntity member, BankEntity bank, BigDecimal investmentPrincipal) {
        AssetEntity asset = createAsset(member, AssetType.FUND, investmentPrincipal.longValue());
        FundEntity fund = FundEntity.builder()
                .bank(bank)
                .asset(asset)
                .investmentPrincipal(investmentPrincipal)
                .fundAmount(investmentPrincipal.multiply(BigDecimal.valueOf(1.1)))
                .profitRatio(BigDecimal.valueOf(10.0))
                .currencyCode(CurrencyCodeType.KRW)
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

    private void createVirtualAsset(MemberEntity member, ExchangeEntity exchange, String name, BigDecimal quantity, BigDecimal currentPrice) {
        AssetEntity asset = createAsset(member, AssetType.VIRTUAL_ASSET, currentPrice.longValue());
        VirtualAsset virtualAsset = VirtualAsset.builder()
                .asset(asset)
                .exchange(exchange)
                .virtualAssetName(name)
                .quantity(quantity)
                .currentPrice(currentPrice)
                .currencyCode(CurrencyCodeType.KRW)
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

