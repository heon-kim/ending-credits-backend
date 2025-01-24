package com.hanaro.endingcredits.endingcreditsapi.utils.adapter.dto.clova.OCR;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClovaICImageDto {
    private String uid;
    private String name;
    private String inferResult;
    private String message;
    private Object validationResult;
    private ClovaICIdCardDto idCard;
}
