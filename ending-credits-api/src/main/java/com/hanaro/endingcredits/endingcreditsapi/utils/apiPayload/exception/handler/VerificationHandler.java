package com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.exception.handler;

import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.code.BaseErrorCode;
import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.exception.GeneralException;

public class VerificationHandler extends GeneralException {
    public VerificationHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
