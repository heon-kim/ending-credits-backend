package com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities;

import com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities.bank.DepositEntity;
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
@ToString(exclude = "deposit")
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
    @JoinColumn(name = "deposit_id")
    private DepositEntity deposit;

    @Column(nullable = false, name = "total_amount")
    private BigDecimal totalAmount; //대출금액

    @Column(nullable = false, name = "loan_amount")
    private BigDecimal loanAmount;  //대출잔액

    @Column(nullable = false, name="expiry_date")
    @Comment("년,월,일만(시간x)")
    private LocalDate expiryDate;   //만기일
}
