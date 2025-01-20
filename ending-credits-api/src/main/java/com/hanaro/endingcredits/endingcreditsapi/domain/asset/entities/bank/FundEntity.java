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
public class FundEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "fund_id")
    private UUID fundId; //펀드ID

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "asset_id")
    private AssetEntity asset; //자산ID

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "bank_id")
    private BankEntity bank; //은행ID

    @Column(nullable = false, name = "investment_principal", columnDefinition = "DECIMAL(19, 2) DEFAULT 0.00")
    @Comment("USD일 경우를 고려해서 default를 0.00으로 설정했습니다.")
    private BigDecimal investmentPrincipal; //투자원금, default: 0.00

    @Column(nullable = false, name = "fund_amount", columnDefinition = "DECIMAL(19, 2) DEFAULT 0.00")
    @Comment("USD일 경우를 고려해서 default를 0.00으로 설정했습니다.")
    private BigDecimal fundAmount; //평가금액, default: 0.00

    @Column(nullable = false, name = "profit_ratio", columnDefinition = "DECIMAL(19, 2) DEFAULT 0.00")
    @Comment("USD일 경우를 고려해서 default를 0.00으로 설정했습니다.")
    private BigDecimal profitRatio; //수익율, default: 0.00

    @Column(nullable = false, name = "currency_code")
    @Comment("KRW: 원화, USD: 미국달러")
    private CurrencyCodeType currencyCode; //통화코드

    @Column(nullable = false, name = "isConnected")
    private boolean isConnected = false;

    public void setConnected(boolean connected) {
        isConnected = connected;
    }
}
