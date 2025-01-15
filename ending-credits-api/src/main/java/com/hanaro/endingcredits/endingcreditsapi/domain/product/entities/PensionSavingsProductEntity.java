package com.hanaro.endingcredits.endingcreditsapi.domain.product.entities;


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
@Table(name = "pension_savings_product")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PensionSavingsProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="product_id")
    private UUID productId;

    @Column(name="product_name")
    private String productName;

    @Column(name="product_area")
    private String productArea;

    @Column(name="product_detail", length = 10000)
    private String productDetail; // JSON 문자열 그대로 저장
}
