package com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities;

import com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities.bank.DepositEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities.bank.FundEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities.bank.TrustEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities.etc.CarEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities.etc.CashEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities.etc.PensionEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities.etc.RealEstateEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities.securities.SecuritiesAccountEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities.virtual.VirtualAsset;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.enums.AssetType;
import com.hanaro.endingcredits.endingcreditsapi.domain.member.entities.MemberEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@ToString(exclude = {"member", "virtualAssets", "securitiesAccounts", "realEstates", "pensions", "cashes", "trusts", "cars", "funds", "deposits"})
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor(access=AccessLevel.PRIVATE)
public class AssetEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "asset_id")
    private UUID assetId; //자산ID

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false) //identifier 참조해야함
    private MemberEntity member; //memberId 있으면 자산연결O, 없으면 연결X

    @Column(nullable=false,name = "asset_type")
    @Enumerated(EnumType.STRING)
    @Comment("DEPOSIT: 예금, FUND: 펀드, TRUST: 신탁, SECURITIES: 증권, VIRTUAL_ASSET: 가상자산, CASH: 현금, " +
            "REAL_ESTATE: 부동산, CAR: 자동차, PENSION: 연금")
    private AssetType assetType;

    @Column(nullable=false, columnDefinition = "INTEGER DEFAULT 0")
    @Comment("자산별 총액은 마지막에 원화로 계산하므로, default: 0")
    private Long amount; //자산별 총액

    @OneToMany(mappedBy = "asset", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VirtualAsset> virtualAssets = new ArrayList<>();

    @OneToMany(mappedBy = "asset", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SecuritiesAccountEntity> securitiesAccounts = new ArrayList<>();

    @OneToMany(mappedBy = "asset", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RealEstateEntity> realEstates = new ArrayList<>();

    @OneToMany(mappedBy = "asset", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PensionEntity> pensions = new ArrayList<>();

    @OneToOne(mappedBy = "asset", cascade = CascadeType.ALL, orphanRemoval = true)
    private CashEntity cashEntity;

    @OneToMany(mappedBy = "asset", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TrustEntity> trusts = new ArrayList<>();

    @OneToMany(mappedBy = "asset", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CarEntity> cars = new ArrayList<>();

    @OneToMany(mappedBy = "asset", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FundEntity> funds = new ArrayList<>();

    @OneToMany(mappedBy = "asset", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DepositEntity> deposits = new ArrayList<>();

    public void setAmount(Long amount) {
        this.amount = amount;
    }
}