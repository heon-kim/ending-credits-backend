package com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities.etc;

import com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities.AssetEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.enums.CurrencyCodeType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@ToString(exclude = "asset")
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CashEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "cash_id")
    private UUID cashId;

    @Column(name = "currency_code")
    @Comment("KRW: 원화, USD: 미국달러")
    private CurrencyCodeType currencyCode; //

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "asset_id")
    private AssetEntity asset;

    @Column(nullable = false,columnDefinition = "DECIMAL(19, 2) DEFAULT 0.00")
    @Comment("USD일 경우를 고려해서 default를 0.00으로 설정했습니다.")
    private BigDecimal amount; //금액 default: 0.00
}
