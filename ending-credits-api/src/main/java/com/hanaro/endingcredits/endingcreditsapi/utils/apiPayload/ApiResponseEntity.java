package com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.code.status.SuccessStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"code", "message", "result"})
public class ApiResponseEntity<T> {
    private final String code;
    private final String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T result;

    public static <T> ApiResponseEntity<T> onSuccess(T result){
        return new ApiResponseEntity<>(SuccessStatus._OK.getCode(), SuccessStatus._OK.getMessage(), result);
    }

    public static <T> ApiResponseEntity<T> onSuccess(String message, T result) {
        return new ApiResponseEntity<>(SuccessStatus._OK.getCode(), message, result);
    }

    public static <T> ApiResponseEntity<T> onNoContentSuccess() {
        return new ApiResponseEntity<>(SuccessStatus._NO_CONTENT.getCode(), SuccessStatus._NO_CONTENT.getMessage(), null);
    }

    public static <T> ApiResponseEntity<T> onFailure(String code, String message, T result) {
        return new ApiResponseEntity<>(code, message, result);
    }
}
