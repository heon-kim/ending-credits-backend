package com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities.etc;

import com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities.AssetEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.enums.PensionType;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.enums.RealEstateType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.UUID;

@Entity
@Getter
@ToString(exclude = "asset")
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RealEstateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "real_estate_id")
    private UUID realEstateId;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "asset_id")
    private AssetEntity asset;

    @Column(nullable = false, name = "realestate_type")
    @Comment("APARTMENT: 아파트, VILLA: 빌라, OFFICETEL: 오피스텔, HOUSE: 주택, LAND: 토지")
    private RealEstateType realEstateType;

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

    @Column(nullable = false, name = "is_connected")
    private boolean isConnected = false;

    public void setConnected(boolean connected) {
        isConnected = connected;
    }
}
