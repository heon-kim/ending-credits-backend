package com.hanaro.endingcredits.endingcreditsapi.domain.product.entities;

import com.hanaro.endingcredits.endingcreditsapi.utils.annotations.JsonListConverter;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Entity
@Getter
@Setter(AccessLevel.PROTECTED)
@Table(name = "retirement_pension_yield")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RetirementPensionYieldEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="company_yield_id")
    private UUID companyYieldId;

    @Column(name="company", nullable = false, length = 50)
    private String company;

    @Enumerated(EnumType.STRING)
    @Column(name="area", nullable = false)
    private ProductArea area;

    @Enumerated(EnumType.STRING)
    @Column(name="sys_type", nullable = false)
    private SysType sysType;

    @Column(name="yield_detail", length = 10000)
    @Convert(converter = JsonListConverter.class)
    private List<Map<String, Object>> yieldDetail;

    public List<Map<String, Object>> getYieldDetail() {
        return this.yieldDetail;
    }
}
