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
    @Comment("연금 수령 연령")
    private Integer pensionAge; //연금 수령 연령

    @Column(nullable = false,columnDefinition = "INTEGER DEFAULT 0")
    private Long amount;

    @Column(nullable = false, name = "isConnected")
    private boolean isConnected = false;

    public void setConnected(boolean connected) {
        isConnected = connected;
    }
}
