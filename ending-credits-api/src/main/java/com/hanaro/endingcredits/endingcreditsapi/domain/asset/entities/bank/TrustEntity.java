package com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities.bank;

import com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities.AssetEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.enums.CurrencyCodeType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@ToString(exclude = "asset")
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TrustEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "trust_id")
    private UUID trustId; //신탁ID

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "bank_id")
    private BankEntity bank; //은행ID

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "asset_id")
    private AssetEntity asset; //자산ID

    @Column(nullable = false, columnDefinition = "DECIMAL(19, 2) DEFAULT 0.00")
    @Comment("USD일 경우를 고려해서 default를 0.00으로 설정했습니다.")
    private BigDecimal amount; //잔액, default: 0.00

    @Column(nullable = false, name = "account_number")
    private String accountNumber;

    @Column(nullable = false, name = "account_name")
    private String accountName;

    @Column(nullable = false, name = "currency_code")
    @Comment("KRW: 원화, USD: 미국달러")
    private CurrencyCodeType currencyCode;

    @Column(nullable = false, name = "isConnected")
    private boolean isConnected = false;

    public void setConnected(boolean connected) {
        isConnected = connected;
    }
}
