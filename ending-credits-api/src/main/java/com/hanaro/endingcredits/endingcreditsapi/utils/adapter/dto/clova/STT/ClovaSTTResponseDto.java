// ClovaSttResponseDto.java
package com.hanaro.endingcredits.endingcreditsapi.utils.adapter.dto.clova.STT;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClovaSTTResponseDto {
    private String result;
    private String message;
    private String token;
    private String version;
    private ClovaSTTParamsDto params;
    private int progress;
    private Map<String, Object> keywords;
    private List<ClovaSTTSegmentDto> segments;
    private String text;
    private double confidence;
    private List<ClovaSTTSpeakerDto> speakers;
    private List<Object> events;
    private List<Object> eventTypes;
}