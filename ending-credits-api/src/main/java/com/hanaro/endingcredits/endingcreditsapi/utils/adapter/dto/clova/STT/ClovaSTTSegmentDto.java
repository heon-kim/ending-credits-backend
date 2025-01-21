// SegmentDto.java
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
public class ClovaSTTSegmentDto {
    private int start;
    private int end;
    private String text;
    private double confidence;
    private ClovaSTTDiarizationDto diarization;
    private ClovaSTTSpeakerDto speaker;
    private List<List<Object>> words;
    private String textEdited;
}