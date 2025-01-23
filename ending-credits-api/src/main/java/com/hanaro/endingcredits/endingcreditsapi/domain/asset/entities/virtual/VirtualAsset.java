package com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities.virtual;

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
@AllArgsConstructor(access=AccessLevel.PRIVATE)
public class VirtualAsset {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "virtual_asset_id")
    private UUID virtualAssetId;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "asset_id")
    private AssetEntity asset;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "exchange_id")
    private ExchangeEntity exchange;

    @Column(nullable = false, name = "virtual_asset_name")
    private String virtualAssetName;

    @Column(nullable = false, columnDefinition = "DECIMAL(19, 4) DEFAULT 0.0000")
    @Comment("코인 수량은 소수점 아래 4번째 자리까지 가능. default: 0.0000")
    private BigDecimal quantity; //default: 0.0000 -> 코인은 소숫점 아래 4자리 까지 보유 가능

    @Column(nullable = false, name = "purchase_price", columnDefinition = "DECIMAL(19, 2) DEFAULT 0.00")
    @Comment("USD일 경우를 고려해서 default를 0.00으로 설정했습니다.")
    private BigDecimal purchasePrice;

    @Column(nullable = false, name = "current_price", columnDefinition = "DECIMAL(19, 2) DEFAULT 0.00")
    @Comment("USD일 경우를 고려해서 default를 0.00으로 설정했습니다.")
    private BigDecimal currentPrice;

    @Column(nullable = false, name = "profit_ratio", columnDefinition = "DECIMAL(19, 2) DEFAULT 0.00")
    private BigDecimal profitRatio;

    @Column(nullable = false, name = "total_value")
    private BigDecimal totalValue;

    @Column(nullable = false, name="currency_code")
    @Comment("KRW: 원화, USD: 미국달러")
    private CurrencyCodeType currencyCode;

    @Column(nullable = false, name = "is_connected")
    private boolean isConnected = false;

    public void setConnected(boolean connected) {
        isConnected = connected;
    }

}