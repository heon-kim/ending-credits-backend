package com.hanaro.endingcredits.endingcreditsapi.utils.adapter.dto.clova.LLM;


import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ClovaLLMMessageDto {
    private String role;
    private String content;
}
