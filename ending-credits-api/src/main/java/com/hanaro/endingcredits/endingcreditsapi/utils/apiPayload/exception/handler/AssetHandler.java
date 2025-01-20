package com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.exception.handler;

import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.code.BaseErrorCode;
import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.exception.GeneralException;

public class AssetHandler extends GeneralException {
    public AssetHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
