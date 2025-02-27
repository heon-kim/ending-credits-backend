package com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities.etc;

import com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities.AssetEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.member.entities.MemberEntity;
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
public class CarEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "car_id")
    private UUID carId;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "asset_id")
    private AssetEntity asset;

    @Column(nullable = false, name = "car_number")
    private String carNumber;

    @Column(nullable = false, name = "purchase_price", columnDefinition = "INTEGER DEFAULT 0")
    @Comment("원화 가치이므로 기본값: 0")
    private Long purchasePrice; //구매가격

    //Todo : 현재 시장가치 AssetDataService 컬럼에 추가하기
    @Column(nullable = false, name = "current_market_price", columnDefinition = "INTEGER DEFAULT 0")
    @Comment("현재 시장가치 기본값: 0")
    private Long currentMarketPrice; // 현재 시장가치

    @Column(nullable = false,columnDefinition = "INTEGER DEFAULT 0")
    @Comment("주행거리 기본값: 0")
    private Integer mileage;

    @Column(nullable = false)
    private String model;
    //Todo : 제조연도 AssetDataService 컬럼에 추가하기
    @Column(nullable = false, name = "manufacture_year")
    @Comment("제조 연도")
    private Integer manufactureYear;

    @Column(nullable = false, name = "is_connected")
    private boolean isConnected = false;

    public void setConnected(boolean connected) {
        isConnected = connected;
    }
}
