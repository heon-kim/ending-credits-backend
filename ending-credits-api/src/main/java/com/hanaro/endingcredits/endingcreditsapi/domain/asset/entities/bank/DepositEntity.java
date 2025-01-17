package com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities.bank;

import com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities.AssetEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.enums.CurrencyCodeEnum;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;

@Entity
@Getter
@ToString
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DepositEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="deposit_id")
    private Long depositId; //예금ID

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "asset_id")
    private AssetEntity assetId; //자산ID

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "bank_id")
    private BankEntity bankId;

    @Column(nullable = false, columnDefinition = "DECIMAL(19, 2) DEFAULT 0.00")//0.00
    @Comment("USD일 경우를 고려해서 default를 0.00으로 설정했습니다.")
    private BigDecimal amount; //잔액, default: 0.00

    @Column(nullable = false,name = "account_number")
    private String accountNumber; //계좌번호

    @Column(nullable = false, name = "account_name")
    private String accountName; //계좌명

    @Column(nullable = false, name = "currency_code")
    @Comment("KRW: 원화, USD: 미국달러")
    private CurrencyCodeEnum currencyCode; //통화코드
}
