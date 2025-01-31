package com.hanaro.endingcredits.endingcreditsapi.utils.adapter.dto.clova.OCR;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClovaIdCardDetailDto {
    private String text;
    private Formatted formatted;
    private String keyText;
    private double confidenceScore;
    private List<Object> boundingPolys;
    private List<Object> maskingPolys;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Formatted {
        private String value;
    }
}
