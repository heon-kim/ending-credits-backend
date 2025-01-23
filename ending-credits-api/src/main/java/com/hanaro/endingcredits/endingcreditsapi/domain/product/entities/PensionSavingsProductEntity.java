package com.hanaro.endingcredits.endingcreditsapi.domain.product.entities;


import com.hanaro.endingcredits.endingcreditsapi.utils.annotations.JsonListConverter;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "pension_savings_product")
public class PensionSavingsProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="product_id")
    private UUID productId;

    @Column(name="product_name")
    private String productName;

    @Column(name="company", nullable = false, length = 50)
    private String company;

    @Enumerated(EnumType.STRING)
    @Column(name="product_area", nullable = false)
    private ProductArea productArea;

    @Column(name="product_detail", length = 10000)
    @Convert(converter = JsonListConverter.class)
    private List<Map<String, Object>> productDetail;

    public List<Map<String, Object>> getProductDetail() {
        return this.productDetail;
    }
}
