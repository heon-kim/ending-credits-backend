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
import java.util.Map;
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
                        .simplePassword("123400")
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
        String[] bankNames = {"KB국민은행", "신한은행", "하나은행", "우리은행", "IBK기업은행",
                "카카오뱅크", "NH농협은행", "지역농협",
                "케이뱅크", "새마을금고",
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
                "삼성증권", "KB증권", "NH투자증권", "카카오페이증권", "하나증권",
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

        // 부동산 1: 역삼동 비엘106
        String realEstateName1 = "역삼동 비엘106";
        String address1 = "서울 강남구 역삼동 123-45";
        long purchasePrice1 = 295_000_000L; // 2억 9천 500만원
        long currentPrice1 = 295_000_000L;

        RealEstateEntity realEstate1 = RealEstateEntity.builder()
                .asset(asset)
                .realEstateName(realEstateName1)
                .address(address1)
                .purchasePrice(purchasePrice1)
                .currentPrice(currentPrice1)
                .realEstateType(RealEstateType.OFFICETEL) // 아파트 유형 예시
                .build();

        realEstateRepository.save(realEstate1);
        totalAmount += currentPrice1;

        // 부동산 2: 신도림3차 e-편한세상
        String realEstateName2 = "신도림3차 e-편한세상";
        String address2 = "서울 구로구 신도림동 678-90";
        long purchasePrice2 = 1_165_000_000L; // 11억 6천 500만원
        long currentPrice2 = 1_165_000_000L;

        RealEstateEntity realEstate2 = RealEstateEntity.builder()
                .asset(asset)
                .realEstateName(realEstateName2)
                .address(address2)
                .purchasePrice(purchasePrice2)
                .currentPrice(currentPrice2)
                .realEstateType(RealEstateType.APARTMENT) // 아파트 유형 예시
                .build();

        realEstateRepository.save(realEstate2);
        totalAmount += currentPrice2;

        // AssetEntity의 총 자산 업데이트
        asset.setAmount(totalAmount);
        assetRepository.save(asset);
    }



    private void createMultipleCarsForAsset(AssetEntity asset) {
        long totalAmount = 0;

        // 자동차 1: 현대 i30 (소형차)
        String model1 = "현대 i30";
        String carNumber1 = "33가 1234";
        long purchasePrice1 = 25_000_000L; // 2천 5백만 원
        long currentMarketPrice1 = generateRandomMarketPrice(purchasePrice1, 0.8, 1.2); // 현재 시장 가격
        int mileage1 = 30_000; // 주행거리 (30,000km)
        int manufactureYear1 = 2019; // 제조연도

        CarEntity car1 = CarEntity.builder()
                .asset(asset)
                .model(model1)
                .carNumber(carNumber1)
                .purchasePrice(purchasePrice1)
                .currentMarketPrice(currentMarketPrice1)
                .mileage(mileage1)
                .manufactureYear(manufactureYear1)
                .build();

        carRepository.save(car1);
        totalAmount += currentMarketPrice1;

        // 자동차 2: 기아 쏘렌토 (SUV)
        String model2 = "기아 쏘렌토";
        String carNumber2 = "88나 5678";
        long purchasePrice2 = 38_000_000L; // 3천 8백만 원
        long currentMarketPrice2 = generateRandomMarketPrice(purchasePrice2, 0.8, 1.2); // 현재 시장 가격
        int mileage2 = 50_000; // 주행거리 (50,000km)
        int manufactureYear2 = 2021; // 제조연도

        CarEntity car2 = CarEntity.builder()
                .asset(asset)
                .model(model2)
                .carNumber(carNumber2)
                .purchasePrice(purchasePrice2)
                .currentMarketPrice(currentMarketPrice2)
                .mileage(mileage2)
                .manufactureYear(manufactureYear2)
                .build();

        carRepository.save(car2);
        totalAmount += currentMarketPrice2;

        // AssetEntity의 총 자산 업데이트
        asset.setAmount(totalAmount);
        assetRepository.save(asset);
    }

    private void createMultipleSecuritiesAssetsForAsset(AssetEntity asset, List<SecuritiesCompanyEntity> securitiesCompanies) {
        BigDecimal totalAmount = BigDecimal.ZERO;

        // 선택할 증권사 5개
        List<SecuritiesCompanyEntity> selectedCompanies = securitiesCompanies.subList(0, 5);

        for (int i = 0; i < selectedCompanies.size(); i++) {
            BigDecimal depositKRW = BigDecimal.valueOf(2_000_000 + i * 500_000); // 예수금 (KRW)
            BigDecimal principalKRW = BigDecimal.valueOf(7_000_000 + i * 1_000_000); // 원금 (KRW)

            BigDecimal depositUSD = depositKRW.divide(EXCHANGE_RATE, 2, BigDecimal.ROUND_HALF_UP); // 예수금 (USD)
            BigDecimal principalUSD = principalKRW.divide(EXCHANGE_RATE, 2, BigDecimal.ROUND_HALF_UP); // 원금 (USD)

            BigDecimal profitRatio = BigDecimal.valueOf(3 + i * 1.5); // 수익률 예시

            // 증권사명에 맞는 실제 계좌번호 형식 적용
            String securitiesCompanyName = selectedCompanies.get(i).getSecuritiesCompanyName();
            String krwAccountNumber = generateKoreanAccountNumber(securitiesCompanyName);
            String usdAccountNumber = generateUSDAccountNumber();

            // KRW 증권 계좌 생성
            SecuritiesAccountEntity accountKRW = SecuritiesAccountEntity.builder()
                    .asset(asset)
                    .securitiesCompany(selectedCompanies.get(i))
                    .securitiesAccountNumber(krwAccountNumber)
                    .securitiesAccountName(securitiesCompanyName + " 종합계좌")
                    .deposit(depositKRW)
                    .principal(principalKRW)
                    .currencyCode(CurrencyCodeType.KRW)
                    .profitRatio(profitRatio)
                    .build();
            securitiesAccountRepository.save(accountKRW);

            // USD 증권 계좌 생성
            SecuritiesAccountEntity accountUSD = SecuritiesAccountEntity.builder()
                    .asset(asset)
                    .securitiesCompany(selectedCompanies.get(i))
                    .securitiesAccountNumber(usdAccountNumber)
                    .securitiesAccountName(securitiesCompanyName + " 해외주식계좌")
                    .deposit(depositUSD)
                    .principal(principalUSD)
                    .currencyCode(CurrencyCodeType.USD)
                    .profitRatio(profitRatio)
                    .build();
            securitiesAccountRepository.save(accountUSD);

            // KRW 및 USD 금액을 합산하여 자산 총액 업데이트
            totalAmount = totalAmount.add(depositKRW.add(principalKRW)); // KRW 금액 추가
            totalAmount = totalAmount.add(depositUSD.add(principalUSD).multiply(EXCHANGE_RATE)); // USD를 KRW로 변환 후 추가
        }

        // AssetEntity의 총 자산 업데이트
        asset.setAmount(totalAmount.longValue());
        assetRepository.save(asset);
    }

    // 한국 증권사별 계좌번호 생성 (실제 계좌 형식 반영)
    private String generateKoreanAccountNumber(String securitiesCompanyName) {
        switch (securitiesCompanyName) {
            case "한국투자증권": return "028-12-345678";
            case "키움증권": return "546-34-123456";
            case "미래에셋증권": return "238-56-789012";
            case "신한투자증권": return "112-77-654321";
            case "삼성증권": return "102-99-876543";
            default: return "000-00-000000"; // 기본값
        }
    }

    // 해외 증권 계좌 번호 생성
    private String generateUSDAccountNumber() {
        return "USD-" + (100000 + (int) (Math.random() * 900000)); // 랜덤 6자리
    }
    private void createMultipleVirtualAssetsForAsset(AssetEntity asset, List<ExchangeEntity> exchanges) {
        BigDecimal totalAmount = BigDecimal.ZERO;

        // 선택할 거래소 5개
        List<ExchangeEntity> selectedExchanges = exchanges.subList(0, 5);

        // 가상자산 목록 (거래소별 대표 가상자산)
        Map<String, List<String>> exchangeCryptoMap = Map.of(
                "업비트", List.of("비트코인 (BTC)", "이더리움 (ETH)"),
                "빗썸", List.of("리플 (XRP)", "솔라나 (SOL)"),
                "코인원", List.of("에이다 (ADA)", "도지코인 (DOGE)"),
                "코빗", List.of("폴카닷 (DOT)", "체인링크 (LINK)"),
                "고팍스", List.of("샌드박스 (SAND)", "디센트럴랜드 (MANA)")
        );

        for (int i = 0; i < selectedExchanges.size(); i++) {
            ExchangeEntity exchange = selectedExchanges.get(i);
            List<String> cryptoNames = exchangeCryptoMap.getOrDefault(exchange.getExchangeName(), List.of("알트코인 (ALT)", "스테이블코인 (STB)"));

            for (String cryptoName : cryptoNames) {
                BigDecimal quantity = BigDecimal.valueOf(1.5 + i * 0.5); // 보유량
                BigDecimal purchasePriceKRW = BigDecimal.valueOf(2_000_000 + i * 500_000); // 구매 가격 (KRW)
                BigDecimal currentPriceKRW = BigDecimal.valueOf(2_500_000 + i * 700_000); // 현재 가격 (KRW)

                BigDecimal purchasePriceUSD = purchasePriceKRW.divide(EXCHANGE_RATE, 2, BigDecimal.ROUND_HALF_UP); // 구매 가격 (USD)
                BigDecimal currentPriceUSD = currentPriceKRW.divide(EXCHANGE_RATE, 2, BigDecimal.ROUND_HALF_UP); // 현재 가격 (USD)

                // KRW 가상 자산 생성
                VirtualAsset assetKRW = VirtualAsset.builder()
                        .asset(asset)
                        .exchange(exchange)
                        .virtualAssetName(cryptoName) // 가상자산명 적용
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
                        .exchange(exchange)
                        .virtualAssetName(cryptoName) // 가상자산명 동일 적용
                        .quantity(quantity)
                        .purchasePrice(purchasePriceUSD)
                        .currentPrice(currentPriceUSD)
                        .profitRatio(calculateProfitRatio(purchasePriceUSD, currentPriceUSD))
                        .totalValue(quantity.multiply(currentPriceUSD).multiply(EXCHANGE_RATE)) // 총 가치 (KRW 환산)
                        .currencyCode(CurrencyCodeType.USD)
                        .build();
                virtualAssetRepository.save(assetUSD);

                // KRW 및 USD 금액을 합산하여 자산 총액 계산
                totalAmount = totalAmount.add(quantity.multiply(currentPriceKRW)); // KRW 총 가치 추가
                totalAmount = totalAmount.add(quantity.multiply(currentPriceUSD).multiply(EXCHANGE_RATE)); // USD 총 가치를 KRW로 변환 후 추가
            }
        }

        // AssetEntity의 총 자산 업데이트
        asset.setAmount(totalAmount.longValue());
        assetRepository.save(asset);
    }

    private void createMultipleFundsForAsset(AssetEntity asset, List<BankEntity> banks) {
        BigDecimal totalAmount = BigDecimal.ZERO;

        // 선택할 은행 5개만 사용
        List<BankEntity> selectedBanks = banks.subList(0, 5);

        // 은행별 펀드 상품명 매핑
        Map<String, String> fundNames = Map.of(
                "KB국민은행", "KB국민 펀드",
                "신한은행", "신한 글로벌 펀드",
                "하나은행", "하나 미래 펀드",
                "우리은행", "우리 가치 성장 펀드",
                "IBK기업은행", "IBK 혁신 펀드"
        );

        for (int i = 0; i < selectedBanks.size(); i++) {
            BankEntity bank = selectedBanks.get(i);
            String fundName = fundNames.getOrDefault(bank.getBankName(), "일반 펀드");

            BigDecimal principalKRW = BigDecimal.valueOf(5_000_000 + i * 500_000); // 투자 원금 (KRW)
            BigDecimal fundAmountKRW = principalKRW.multiply(BigDecimal.valueOf(1.12)); // 수익 포함 (KRW)

            // 펀드 자산 생성
            FundEntity fundKRW = FundEntity.builder()
                    .bank(bank)
                    .asset(asset)
                    .accountName(fundName) // ✅ 현실적인 펀드명 적용
                    .accountNumber("펀드-" + bank.getBankName().replace("은행", "") + "-65" + (30 + i)) // ✅ 계좌번호 변경
                    .investmentPrincipal(principalKRW)
                    .fundAmount(fundAmountKRW) // 수익 포함 금액 (KRW)
                    .profitRatio(calculateProfitRatio(principalKRW, fundAmountKRW)) // 수익률
                    .currencyCode(CurrencyCodeType.KRW)
                    .build();

            fundRepository.save(fundKRW);

            // 총 금액 업데이트
            totalAmount = totalAmount.add(fundAmountKRW);
        }

        // AssetEntity의 총 자산 업데이트
        asset.setAmount(totalAmount.longValue());
        assetRepository.save(asset);
    }

    private void createMultipleTrustsForAsset(AssetEntity asset, List<BankEntity> banks) {
        BigDecimal totalAmount = BigDecimal.ZERO;

        // 선택할 은행 5개만 사용
        List<BankEntity> selectedBanks = banks.subList(0, 5);

        // 은행별 신탁 상품명 매핑
        Map<String, String> trustNames = Map.of(
                "KB국민은행", "KB국민 신탁",
                "신한은행", "신한 자산 신탁",
                "하나은행", "하나 프리미엄 신탁",
                "우리은행", "우리 플러스 신탁",
                "IBK기업은행", "IBK 기업 자산 신탁"
        );

        for (int i = 0; i < selectedBanks.size(); i++) {
            BankEntity bank = selectedBanks.get(i);
            String trustName = trustNames.getOrDefault(bank.getBankName(), "일반 신탁");

            BigDecimal amountKRW = BigDecimal.valueOf(3_000_000 + i * 200_000); // 신탁 금액 (KRW)

            // 신탁 자산 생성
            TrustEntity trustKRW = TrustEntity.builder()
                    .bank(bank)
                    .asset(asset)
                    .accountName(trustName) // ✅ 현실적인 신탁명 적용
                    .accountNumber("신탁-" + bank.getBankName().replace("은행", "") + "-65" + (30 + i)) // ✅ 계좌번호 변경
                    .amount(amountKRW)
                    .currencyCode(CurrencyCodeType.KRW)
                    .build();

            trustRepository.save(trustKRW);

            // 총 금액 업데이트
            totalAmount = totalAmount.add(amountKRW);
        }

        // AssetEntity의 총 자산 업데이트
        asset.setAmount(totalAmount.longValue());
        assetRepository.save(asset);
    }

    private void createMultipleDepositsForAsset(AssetEntity asset, List<BankEntity> banks) {
        BigDecimal totalAmount = BigDecimal.ZERO;
        DepositEntity deposit = null;

        // 선택할 은행 5개만 사용
        List<BankEntity> selectedBanks = banks.subList(0, 5);

        // 은행별 예금 상품명 매핑
        Map<String, String> depositNames = Map.of(
                "KB국민은행", "KB국민 주거래 예금",
                "신한은행", "신한 스마트 예금",
                "하나은행", "하나 플러스 예금",
                "우리은행", "우리 프리미엄 예금",
                "IBK기업은행", "IBK 기업 맞춤 예금"
        );

        for (int i = 0; i < selectedBanks.size(); i++) {
            BankEntity bank = selectedBanks.get(i);
            String depositName = depositNames.getOrDefault(bank.getBankName(), "일반 예금");

            BigDecimal amountKRW = BigDecimal.valueOf(5_000_000 + i * 500_000); // 예금 금액 (KRW)

            // 예금 자산 생성
            DepositEntity depositKRW = DepositEntity.builder()
                    .bank(bank)
                    .asset(asset)
                    .accountName(depositName) // ✅ 현실적인 예금명 적용
                    .accountNumber("예금-" + bank.getBankName().replace("은행", "") + "-65" + (40 + i)) // ✅ 계좌번호 변경
                    .amount(amountKRW)
                    .currencyCode(CurrencyCodeType.KRW)
                    .build();

            deposit = depositRepository.save(depositKRW);
            totalAmount = totalAmount.add(amountKRW);
        }

        if (deposit != null) {
            createLoan(deposit);
        }

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

