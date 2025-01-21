package com.hanaro.endingcredits.endingcreditsapi.domain.asset.dto;

import com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities.etc.CarEntity;
import lombok.Data;

import java.util.UUID;

@Data
public class CarDetailDto {
    private UUID carId;
    private String model;
    private String carNumber;
    private Long purchasePrice;
    private Long currentPurchasePrice;
    private Integer mileage;
    private Integer year;

    public static CarDetailDto fromEntity(CarEntity car) {
        CarDetailDto dto = new CarDetailDto();
        dto.setCarId(car.getCarId());
        dto.setModel(car.getModel());
        dto.setCarNumber(car.getCarNumber());
        dto.setPurchasePrice(car.getPurchasePrice());
        dto.setCurrentPurchasePrice(car.getPurchasePrice());
        dto.setMileage(car.getMileage());
        dto.setYear(car.getManufactureYear());
        return dto;
    }
}
