package com.hanaro.endingcredits.endingcreditsapi.domain.product.entities;


import com.hanaro.endingcredits.endingcreditsapi.utils.annotations.JsonListConverter;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Entity
@Getter
@Setter(AccessLevel.PROTECTED)
@Table(name = "retirement_pension_product")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RetirementPensionProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="product_id")
    private UUID productId;

    @Column(name="product_name")
    private String productName;

    @Column(name="product_area")
    private String productArea;

    @Column(name="sys_type")
    private String sysType;

    @Column(name="product_detail", length = 10000)
    @Convert(converter = JsonListConverter.class)
    private List<Map<String, Object>> productDetail;
}
