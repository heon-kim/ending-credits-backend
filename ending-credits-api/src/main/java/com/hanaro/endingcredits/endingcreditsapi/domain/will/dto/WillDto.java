package com.hanaro.endingcredits.endingcreditsapi.domain.will.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WillDto {
    private List<InheritanceDto> inheritances;
    private List<ExecutorDto> executors;
    private List<FinalMessageDto> finalMessages;
}
