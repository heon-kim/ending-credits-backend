package com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities.etc;

import com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities.AssetEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.enums.PensionEnum;
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
public class PensionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pension_id")
    private Long pensionId;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "asset_id")
    private AssetEntity assetId;

    @Column(nullable = false, name = "pension_type")
    @Comment("NATIONAL: 국민연금, RETIREMENT: 퇴직연금, PERSONAL: 개인연금")
    private PensionEnum pensionType; //연금종류

    @Column(nullable = false, name = "pension_name")
    private String pensionName;

    @Column(nullable = false, name = "pension_age")
    @Comment("연금 수령 연령")
    private Integer pensionAge; //연금 수령 연령

    @Column(nullable = false,columnDefinition = "INTEGER DEFAULT 0")
    private Long amount;
}
