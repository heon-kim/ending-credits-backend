package com.hanaro.endingcredits.endingcreditsapi.domain.asset.dto;

import lombok.Getter;

import java.util.UUID;

@Getter
public class AddRealEstateRequest {
    private UUID memberId;
    private String name;
    private String address;
    private Long purchasePrice;
    private Long currentPrice;
}