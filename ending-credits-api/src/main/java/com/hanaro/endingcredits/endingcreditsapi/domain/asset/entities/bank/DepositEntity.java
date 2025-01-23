package com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities.bank;

import com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities.AssetEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities.LoanEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.enums.CurrencyCodeType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@ToString(exclude = "asset")
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DepositEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="deposit_id")
    private UUID depositId; //예금ID

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "asset_id")
    private AssetEntity asset; //자산ID

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_id")
    private BankEntity bank;

    @Column(nullable = false, columnDefinition = "DECIMAL(19, 2) DEFAULT 0.00")//0.00
    @Comment("USD일 경우를 고려해서 default를 0.00으로 설정했습니다.")
    private BigDecimal amount; //잔액, default: 0.00

    @Column(nullable = false,name = "account_number")
    private String accountNumber; //계좌번호

    @Column(nullable = false, name = "account_name")
    private String accountName; //계좌명

    @Column(nullable = false, name = "currency_code")
    @Comment("KRW: 원화, USD: 미국달러")
    private CurrencyCodeType currencyCode; //통화코드

    @Column(nullable = false, name = "is_connected")
    private boolean isConnected = false;

    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    @OneToMany(mappedBy = "deposit", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<LoanEntity> loans = new ArrayList<>();
}
