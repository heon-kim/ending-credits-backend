package com.hanaro.endingcredits.endingcreditsapi.domain.will.dto;

import com.hanaro.endingcredits.endingcreditsapi.domain.will.entities.WillCreatedType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WillRequestDto {
    private String willCodeId;
    private WillCreatedType createdType;
    private List<String> files;
    private Integer shareAt;
}
