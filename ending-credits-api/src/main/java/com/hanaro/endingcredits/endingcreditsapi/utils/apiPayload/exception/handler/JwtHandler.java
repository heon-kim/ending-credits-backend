package com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.exception.handler;

import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.code.BaseErrorCode;
import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.exception.InvalidJwtException;

public class JwtHandler extends InvalidJwtException {
    public JwtHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
