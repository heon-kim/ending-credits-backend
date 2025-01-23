package com.hanaro.endingcredits.endingcreditsapi.utils.adapter.dto.clova.LLM;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClovaLLMResultDto {
    private ClovaLLMMessageDto message;
    private int inputLength;
    private int outputLength;
    private String stopReason;
    private long seed;
}
