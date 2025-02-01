package com.hanaro.endingcredits.endingcreditsapi.utils.adapter.dto.clova.OCR.will;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClovaOCRWillResponseDto {
    private String version;
    private String requestId;
    private Long timestamp;
    private ClovaOCRWillImageDto[] images;
}
