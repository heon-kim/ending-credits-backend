package com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities.etc;

import com.hanaro.endingcredits.endingcreditsapi.domain.member.entities.MemberEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@ToString
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CarEntity {
    @Id
    @GeneratedValue
    @Column(name = "car_number")
    @Comment("공백없이, 예: 12가1111, 105허1111")
    private String carNumber;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "member_id")
    private MemberEntity memberId;

    @Column(nullable = false, name = "purchase_price", columnDefinition = "INTEGER DEFAULT 0")
    @Comment("원화 가치이므로 기본값: 0")
    private Long purchasePrice; //구매가격

    @Column(nullable = false,columnDefinition = "INTEGER DEFAULT 0")
    @Comment("주행거리 기본값: 0")
    private Integer mileage;

    @Column(nullable = false)
    private String model;
}
