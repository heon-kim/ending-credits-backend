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
public class ClovaICDto {
    private List<ClovaIdCardDetailDto> name;
    private List<ClovaIdCardDetailDto> personalNum;
    private List<ClovaIdCardDetailDto> address;
    private List<Object> issueDate;
    private List<Object> authority;
}
