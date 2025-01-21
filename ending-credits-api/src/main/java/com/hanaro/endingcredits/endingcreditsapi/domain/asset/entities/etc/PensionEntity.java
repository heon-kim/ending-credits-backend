package com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities.etc;

import com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities.AssetEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.enums.PensionType;
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
public class PensionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "pension_id")
    private UUID pensionId;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "asset_id")
    private AssetEntity asset;

    @Column(nullable = false, name = "pension_type")
    @Comment("NATIONAL: 국민연금, RETIREMENT: 퇴직연금, PERSONAL: 개인연금")
    private PensionType pensionType; //연금종류

    @Column(nullable = false, name = "pension_age")
    @Comment("연금 수령 시작 연령")
    private Integer pensionAge; // 연금 수령 시작 연령

    //Todo : 이 밑의 3가지 종류 컬럼 값 AssetDataService에 추가해야함
    @Column(nullable = false, name = "monthly_payment", columnDefinition = "INTEGER DEFAULT 0")
    @Comment("매월 수령 금액 (기본값: 0)")
    private Long monthlyPayment; // 매월 수령 금액

    @Column(nullable = false, name = "payment_duration", columnDefinition = "INTEGER DEFAULT 0")
    @Comment("수령 기간 (단위: 연, 기본값: 0)")
    private Integer paymentDuration; // 연금 수령 기간 (단위: 연)

    @Column(nullable = false, name = "total_expected_amount", columnDefinition = "INTEGER DEFAULT 0")
    @Comment("총 예상 수령 금액 (기본값: 0)")
    private Long totalExpectedAmount; // 총 예상 수령 금액

    @Column(nullable = false, name = "is_connected")
    private boolean isConnected = false;

    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    public void calculateTotalExpectedAmount() {
        this.totalExpectedAmount = this.monthlyPayment * 12 * this.paymentDuration;
    }
}
