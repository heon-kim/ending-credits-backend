package com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities.securities;

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
public class SecuritiesAccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="securities_account_id")
    private UUID securitiesAccountId; //증권계좌ID

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "asset_id")
    private AssetEntity asset;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "securities_company_id")
    private SecuritiesCompanyEntity securitiesCompany; //증권사 ID

    @Column(nullable = false, name = "securities_account_name")
    private String securitiesAccountName; //계좌명
    
    @Column(nullable = false, columnDefinition = "DECIMAL(19, 2) DEFAULT 0.00")
    @Comment("USD일 경우를 고려해서 default를 0.00으로 설정했습니다.")
    private BigDecimal deposit; //예수금: 주식 안 산 나머지 돈(운용 가능한 돈), default: 0.00
    
    @Column(nullable = false, columnDefinition = "DECIMAL(19, 2) DEFAULT 0.00")
    @Comment("USD일 경우를 고려해서 default를 0.00으로 설정했습니다.")
    private BigDecimal principal; //원금: 예수금 포함, 계좌에 들어간 모든 돈, default: 0.00
    
    @Column(nullable = false,name = "currency_code")
    @Comment("KRW: 원화, USD: 미국달러")
    private CurrencyCodeType currencyCode; //통화코드

    @Column(nullable = false, name = "isConnected")
    private boolean isConnected = false;

    @Column(nullable = false, name = "profit_ratio", columnDefinition = "DECIMAL(19, 2) DEFAULT 0.00")
    private BigDecimal profitRatio; //수익율, default: 0.00

    public void setConnected(boolean connected) {
        isConnected = connected;
    }
}
