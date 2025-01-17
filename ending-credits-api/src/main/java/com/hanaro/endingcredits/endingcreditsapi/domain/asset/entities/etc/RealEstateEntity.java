package com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities.etc;

import com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities.AssetEntity;
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
public class RealEstateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "real_estate_id")
    private Long realEstateId;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "asset_id")
    private AssetEntity assetId;

    @Column(nullable = false, name = "purchase_price",columnDefinition = "INTEGER DEFAULT 0")
    @Comment("원화뿐이므로 default: 0")
    private Long purchasePrice; //구매가격

    @Column(nullable = false, name = "current_price", columnDefinition = "INTEGER DEFAULT 0")
    @Comment("원화뿐이므로 default: 0")
    private Long currentPrice; //현재가격

    @Column(nullable = false, name = "real_estate_name")
    private String realEstateName;

    @Column(nullable = false)
    private String address;
}
