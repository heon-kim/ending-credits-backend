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
@Table(name = "retirement_pension_company")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RetirementPensionCompanyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="company_id")
    private UUID companyId;

    @Column(name="company", nullable = false, length = 50)
    private String company;

    @Enumerated(EnumType.STRING)
    @Column(name="area", nullable = false)
    private ProductArea area;

    @Column(name="yield_details", length = 10000)
    @Convert(converter = JsonListConverter.class)
    private List<Map<String, Object>> yieldDetails;

    @Column(name="fee_details", length = 10000)
    @Convert(converter = JsonListConverter.class)
    private List<Map<String, Object>> feeDetails;

    public void setFeeDetails(List<Map<String, Object>> feeDetails) {
        this.feeDetails = feeDetails;
    }
}
