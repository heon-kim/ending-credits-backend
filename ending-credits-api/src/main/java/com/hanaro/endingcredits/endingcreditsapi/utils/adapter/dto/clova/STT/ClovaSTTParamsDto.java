// ParamsDto.java
package com.hanaro.endingcredits.endingcreditsapi.utils.adapter.dto.clova.STT;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClovaSTTParamsDto {
    private String service;
    private String domain;
    private String lang;
    private String completion;
    private ClovaSTTDiarizationDto diarization;
    private ClovaSTTSedDto sed;
    private List<Object> boostings;
    private String forbiddens;
    private boolean wordAlignment;
    private boolean fullText;
    private boolean noiseFiltering;
    private boolean resultToObs;
    private int priority;
    private ClovaSTTUserdataDto userdata;
}