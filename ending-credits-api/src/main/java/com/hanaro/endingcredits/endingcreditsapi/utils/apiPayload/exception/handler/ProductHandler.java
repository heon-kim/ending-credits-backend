package com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.exception.handler;

import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.code.BaseErrorCode;
import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.exception.GeneralException;

public class ProductHandler extends GeneralException {
    public ProductHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
