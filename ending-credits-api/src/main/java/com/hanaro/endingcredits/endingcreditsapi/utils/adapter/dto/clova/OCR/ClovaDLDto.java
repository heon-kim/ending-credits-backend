package com.hanaro.endingcredits.endingcreditsapi.utils.adapter.dto.clova.OCR;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClovaDLDto {
    private List<Object> type;
    private List<Object> num;
    private List<ClovaIdCardDetailDto> name;
    private List<ClovaIdCardDetailDto> personalNum;
    private List<ClovaIdCardDetailDto> address;
    private List<Object> renewStartDate;
    private List<Object> renewEndDate;
    private List<Object> code;
    private List<Object> issueDate;
    private List<Object> authority;
}
