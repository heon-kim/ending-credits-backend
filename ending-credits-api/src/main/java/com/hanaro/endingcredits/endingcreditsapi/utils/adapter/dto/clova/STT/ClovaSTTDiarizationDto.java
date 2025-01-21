// DiarizationDto.java
package com.hanaro.endingcredits.endingcreditsapi.utils.adapter.dto.clova.STT;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClovaSTTDiarizationDto {
    private boolean enable;
    private int speakerCountMin;
    private int speakerCountMax;
}