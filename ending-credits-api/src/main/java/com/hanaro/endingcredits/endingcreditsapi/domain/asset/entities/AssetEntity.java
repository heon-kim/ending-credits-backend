package com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities;

import com.hanaro.endingcredits.endingcreditsapi.domain.asset.enums.AssetEnum;
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
@AllArgsConstructor(access=AccessLevel.PRIVATE)
public class AssetEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "asset_id")
    private Long assetId; //자산ID

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "member_id") //identifier 참조해야함
    private MemberEntity memberId; //memberId 있으면 자산연결O, 없으면 연결X

    @Column(nullable=false,name = "asset_type")
    @Enumerated(EnumType.STRING)
    @Comment("BANK: 은행, SECURITIES: 증권, VIRTUAL_ASSET: 가상자산, CASH: 현금, " +
            "REAL_ESTATE: 부동산, CAR: 자동차, PENSION: 연금")
    private AssetEnum assetType;

    @Column(nullable=false, columnDefinition = "INTEGER DEFAULT 0")
    @Comment("자산별 총액은 마지막에 원화로 계산하므로, default: 0")
    private Long amount; //자산별 총액
}