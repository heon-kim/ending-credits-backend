package com.hanaro.endingcredits.endingcreditsapi.domain.product.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "product_detail")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Setter
public class ProductDetailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private PensionSavingsProductEntity product;

    @Column(name = "earn_rate")
    private Double earnRate;

    @Column(name = "earn_rate1")
    private Double earnRate1;

    @Column(name = "earn_rate2")
    private Double earnRate2;

    @Column(name = "earn_rate3")
    private Double earnRate3;

    @Column(name = "avg_earn_rate3")
    private Double avgEarnRate3;

    @Column(name = "avg_earn_rate5")
    private Double avgEarnRate5;

    @Column(name = "avg_earn_rate7")
    private Double avgEarnRate7;

    @Column(name = "avg_earn_rate10")
    private Double avgEarnRate10;

    @Column(name = "fee_rate1")
    private Double feeRate1;

    @Column(name = "fee_rate2")
    private Double feeRate2;

    @Column(name = "fee_rate3")
    private Double feeRate3;

    @Column(name = "avg_fee_rate3")
    private Double avgFeeRate3;

    @Column(name = "balance")
    private Integer balance;

    @Column(name = "balance1")
    private Integer balance1;

    @Column(name = "balance2")
    private Integer balance2;

    @Column(name = "balance3")
    private Integer balance3;

    @Column(name = "reserve")
    private Integer reserve;

    @Column(name = "reserve1")
    private Integer reserve1;

    @Column(name = "reserve2")
    private Integer reserve2;

    @Column(name = "reserve3")
    private Integer reserve3;

    @Column(name = "product_type")
    private String productType;

    @Column(name = "rcv_method")
    private String rcvMethod;

    @Column(name = "fee_type")
    private String feeType;

    @Column(name = "sells")
    private String sells;

    @Column(name = "withdraws")
    private String withdraws;

    @Column(name = "guarantees")
    private String guarantees;
}