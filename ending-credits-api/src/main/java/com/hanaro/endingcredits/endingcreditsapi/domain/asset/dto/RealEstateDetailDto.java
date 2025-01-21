package com.hanaro.endingcredits.endingcreditsapi.domain.asset.dto;

import com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities.etc.RealEstateEntity;
import lombok.Data;

import java.util.UUID;

@Data
public class RealEstateDetailDto {
    private UUID realEstateId;
    private String realEstateName;
    private String address;
    private Long purchasePrice;
    private Long currentPrice;

    public static RealEstateDetailDto fromEntity(RealEstateEntity entity) {
        RealEstateDetailDto dto = new RealEstateDetailDto();
        dto.setRealEstateId(entity.getRealEstateId());
        dto.setRealEstateName(entity.getRealEstateName());
        dto.setAddress(entity.getAddress());
        dto.setPurchasePrice(entity.getPurchasePrice());
        dto.setCurrentPrice(entity.getCurrentPrice());
        return dto;
    }
}
