package com.hanaro.endingcredits.endingcreditsapi.domain.product.entities;

import com.hanaro.endingcredits.endingcreditsapi.utils.annotations.JsonListConverter;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Map;
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

    @Column(name="company", nullable = false, length = 50)
    private String company;

    @Enumerated(EnumType.STRING)
    @Column(name="product_area", nullable = false)
    private ProductArea productArea;

    @Enumerated(EnumType.STRING)
    @Column(name="sys_type", nullable = false)
    private SysType sysType;

    @Column(name="product_detail", length = 10000)
    @Convert(converter = JsonListConverter.class)
    private List<Map<String, Object>> productDetail;

    public List<Map<String, Object>> getProductDetail() {
        return this.productDetail;
    }
}
