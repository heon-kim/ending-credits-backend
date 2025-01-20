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

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "asset_id")
    private AssetEntity asset;

    @Column(nullable = false, columnDefinition = "DECIMAL(19, 0) DEFAULT 0")
    @Comment("금액의 기본값을 0으로 설정합니다.")
    private BigDecimal amount; // 금액 default: 0

    @Column(nullable = false, name = "is_connected")
    private boolean isConnected = false;

    public void setConnected(boolean connected) {
        isConnected = connected;
    }
}