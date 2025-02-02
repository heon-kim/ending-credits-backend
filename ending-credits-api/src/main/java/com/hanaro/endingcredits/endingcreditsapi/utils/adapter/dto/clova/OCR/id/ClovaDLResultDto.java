package com.hanaro.endingcredits.endingcreditsapi.utils.adapter.dto.clova.OCR.id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClovaDLResultDto {
    private Boolean isConfident;
    private ClovaDLDto dl;
    private Object rois;
    private String idType;
}
