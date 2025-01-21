package com.hanaro.endingcredits.endingcreditsapi.domain.product.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;
import java.math.BigDecimal;

@Entity
@Getter
@Setter(AccessLevel.PROTECTED)
@Table(name = "retirement_pension_product")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RetirementPensionProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="product_id")
    private UUID productId;

    @Column(name="product_name", nullable = false)
    private String productName;

    @Enumerated(EnumType.STRING)
    @Column(name="product_area", nullable = false)
    private ProductArea productArea;

    @Enumerated(EnumType.STRING)
    @Column(name="sys_type", nullable = false)
    private SysType sysType;

    @Column(name="company", nullable = false, length = 50)
    private String company;

    @Column(name="apply_term", nullable = false, length = 10)
    private String applyTerm;

    @Column(name="check_date", nullable = false)
    private String checkDate;

    @Column(name="contract_term", nullable = false)
    private int contractTerm;

    @Column(name="contract_rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal contractRate;


    /**
     * 기존 데이터를 업데이트하는 메서드
     */
    public void update(RetirementPensionProductEntity newEntity) {
        this.applyTerm = newEntity.getApplyTerm();
        this.checkDate = newEntity.getCheckDate();
        this.contractTerm = newEntity.getContractTerm();
        this.contractRate = newEntity.getContractRate();
        this.productArea = newEntity.getProductArea();
        this.sysType = newEntity.getSysType();
    }
}
