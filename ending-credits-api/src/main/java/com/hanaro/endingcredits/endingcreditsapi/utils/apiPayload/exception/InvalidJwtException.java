package com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.exception;

import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.code.BaseErrorCode;
import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InvalidJwtException extends RuntimeException {
    private BaseErrorCode code;
    public ErrorReasonDTO getErrorReason() {
        return this.code.getReason();
    }
    public ErrorReasonDTO getErrorReasonHttpStatus(){
        return this.code.getReasonHttpStatus();
    }
}