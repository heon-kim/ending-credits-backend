package com.hanaro.endingcredits.endingcreditsapi.domain.product.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
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

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<ProductDetailEntity> productDetails = new ArrayList<>();

    public void addProductDetail(ProductDetailEntity detail) {
        this.productDetails.add(detail);
        detail.setProduct(this);
    }
}