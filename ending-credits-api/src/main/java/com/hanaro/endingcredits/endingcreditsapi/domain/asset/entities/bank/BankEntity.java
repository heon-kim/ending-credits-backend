package com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities.bank;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@ToString
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor(access=AccessLevel.PRIVATE)
public class BankEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="bank_id")
    private Long bankId; //은행ID, 은행을 분류

    @Column(nullable = false, name="bank_name")
    private String bankName; //은행명

    @Column(nullable = false,name="savings_bank")
    @Comment("false: 일반은행, true: 저축은행")
    private boolean isSavingsBank; //false=일반은행, true=저축은행
}
