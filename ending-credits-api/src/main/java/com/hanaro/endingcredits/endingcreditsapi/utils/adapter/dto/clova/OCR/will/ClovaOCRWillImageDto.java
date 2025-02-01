package com.hanaro.endingcredits.endingcreditsapi.utils.adapter.dto.clova.OCR.will;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClovaOCRWillImageDto {
    private String uid;
    private String name;
    private String inferResult;
    private String message;
    private Object validationResult;
    private Object convertedImageInfo;
    private ClovaOCRWillFieldDto[] fields;
}
