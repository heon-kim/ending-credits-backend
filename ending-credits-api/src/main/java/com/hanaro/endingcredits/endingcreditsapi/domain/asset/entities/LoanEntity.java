package com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities;

import com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities.bank.BankEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities.bank.DepositEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.enums.CurrencyCodeType;
import com.hanaro.endingcredits.endingcreditsapi.domain.member.entities.MemberEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@ToString
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LoanEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "loan_id")
    private UUID loanId;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "member_id")
    private MemberEntity member;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "deposit_id")
    private DepositEntity depositId;

    @Column(nullable = false, name = "total_amount")
    private BigDecimal totalAmount; //대출금액

    @Column(nullable = false, name = "loan_amount")
    private BigDecimal loanAmount; //대출잔액

    @Column(nullable = false, name="currency_code")
    @Comment("KRW: 원화, USD: 미국달러")
    private CurrencyCodeType currencyCode; //통화코드

    @Column(nullable = false, name="expiry_date")
    @Comment("년,월,일만(시간x)")
    private LocalDate expiryDate; //만기일
}
