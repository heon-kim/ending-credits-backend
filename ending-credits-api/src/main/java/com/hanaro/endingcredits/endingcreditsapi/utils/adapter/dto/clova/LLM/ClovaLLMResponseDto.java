package com.hanaro.endingcredits.endingcreditsapi.utils.adapter.dto.clova.LLM;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClovaLLMResponseDto {
    private ClovaLLMStatusDto status;
    private ClovaLLMResultDto result;

}
