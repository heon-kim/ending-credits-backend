package com.hanaro.endingcredits.endingcreditsapi.utils.adapter.dto.clova.OCR;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClovaICIdCardDto {
    private Object meta;
    private ClovaICResultDto result;
}
