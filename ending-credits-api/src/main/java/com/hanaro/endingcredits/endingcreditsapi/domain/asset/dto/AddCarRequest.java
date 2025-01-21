package com.hanaro.endingcredits.endingcreditsapi.domain.asset.dto;

import lombok.Data;
import lombok.Getter;

import java.util.UUID;

@Getter
public class AddCarRequest {
    private UUID memberId;
    private String model;
    private String carNumber;
    private Long purchasePrice;
    private Long currentPurchasePrice;
    private Integer mileage;
    private Integer year;
}
