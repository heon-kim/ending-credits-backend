package com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities.securities;

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
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stock_id")
    private Long stockId; //종목ID

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "securities_company_id")
    private SecuritiesAccountEntity securitiesCompanyId; //증권계좌ID

    @Column(nullable = false, name="stock_name")
    private String stockName; //종목명

    @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private Integer quantity; //수량

    @Column(nullable = false,name = "average_purchase_price", columnDefinition = "DECIMAL(19, 2) DEFAULT 0.00") // default 값 설정 알아보기
    @Comment("USD일 경우를 고려해서 default를 0.00으로 설정했습니다.")
    private BigDecimal averagePurchasePrice; //평균매입가, default: 0.00

    @Column(nullable = false,name = "current_price",columnDefinition = "DECIMAL(19, 2) DEFAULT 0.00") // default 값 설정 알아보기
    @Comment("USD일 경우를 고려해서 default를 0.00으로 설정했습니다.")
    private BigDecimal currentPrice; //현재가, default: 0.00

    @Column(nullable = false,name = "profit_ratio",columnDefinition = "DECIMAL(19, 2) DEFAULT 0.00")
    @Comment("USD일 경우를 고려해서 default를 0.00으로 설정했습니다.")
    private BigDecimal profitRatio; //수익률, default: 0.00

    @Column(nullable = false,name = "currency_code")
    @Comment("KRW: 원화, USD: 미국달러")
    private String currencyCode; //통화코드
}