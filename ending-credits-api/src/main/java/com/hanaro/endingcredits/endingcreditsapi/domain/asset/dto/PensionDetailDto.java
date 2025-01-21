package com.hanaro.endingcredits.endingcreditsapi.domain.asset.dto;

import com.hanaro.endingcredits.endingcreditsapi.domain.asset.entities.etc.PensionEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.asset.enums.PensionType;
import lombok.Data;

import java.util.UUID;

@Data
public class PensionDetailDto {
    private UUID pensionId;
    private PensionType pensionType;
    private int pensionAge;
    private long monthlyPayment;       // 매월 수령 금액
    private int paymentDuration;      // 수령 기간
    private long totalExpectedAmount; // 총 예상 수령 금액

    public static PensionDetailDto fromEntity(PensionEntity entity) {
        PensionDetailDto dto = new PensionDetailDto();
        dto.setPensionId(entity.getPensionId());
        dto.setPensionType(entity.getPensionType());
        dto.setPensionAge(entity.getPensionAge());
        dto.setMonthlyPayment(entity.getMonthlyPayment());
        dto.setPaymentDuration(entity.getPaymentDuration());
        dto.setTotalExpectedAmount(entity.getTotalExpectedAmount());
        return dto;
    }
}
