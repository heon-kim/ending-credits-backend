package com.hanaro.endingcredits.endingcreditsapi.utils.adapter.dto.clova.OCR.id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClovaDLResponseDto {
    private String version;
    private String requestId;
    private Long timestamp;
    private List<ClovaDLImageDto> images;
}
