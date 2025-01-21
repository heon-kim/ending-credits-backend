// UserdataDto.java
package com.hanaro.endingcredits.endingcreditsapi.utils.adapter.dto.clova.STT;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClovaSTTUserdataDto {
    private String _ncp_DomainCode;
    private int _ncp_DomainId;
    private int _ncp_TaskId;
    private String _ncp_TraceId;
}