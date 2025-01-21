package com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.code.status;

import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.code.BaseErrorCode;
import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {
    // 일반적인 응답
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401", "인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),

    // 멤버 관련 에러
    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "MEMBER4001", "존재하지 않는 사용자입니다."),
    WRONG_PASSWORD(HttpStatus.BAD_REQUEST, "MEMBER4001", "비밀번호가 맞지 않습니다."),
    DUPLICATED_IDENTIFIER(HttpStatus.BAD_REQUEST, "MEMBER4001", "중복된 아이디 입니다."),

    // 금융 상품 관련 에러
    PRODUCT_NOT_FOUND(HttpStatus.BAD_REQUEST, "PRODUCT4001", "존재하지 않는 상품입니다."),
    RECOMMEND_NOT_FOUND(HttpStatus.BAD_REQUEST, "PRODUCT4002", "추천상품 결과가 없습니다."),

    // 토근 관련 에러
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "TOKEN4001", "유효하지 않은 토큰입니다."),
    EMPTY_HEADER(HttpStatus.BAD_REQUEST, "TOKEN4001", "헤더가 비어있습니다."),
    INVALID_HEADER(HttpStatus.BAD_REQUEST, "TOKEN4001", "유효하지 않은 헤더 형식입니다."),

    // 번호 인증 관련 에러
    VERIFICATION_CODE_EXPIRED(HttpStatus.BAD_REQUEST, "VERIFICATION4001", "인증 코드가 만료되었습니다."),
    VERIFICATION_CODE_SEND_FAILED(HttpStatus.BAD_REQUEST, "VERIFICATION500", "인증 코드 전송에 실패했습니다."),
    INVALID_VERIFICATION_CODE(HttpStatus.BAD_REQUEST, "VERIFICATION4002", "유효하지 않은 인증 코드입니다."),

    // 자산 관련 에러
    CASH_NOT_FOUND(HttpStatus.BAD_REQUEST, "CASH4001", "현금 자산이 존재하지 않습니다."),
    NEGATIVE_AMOUNT_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "CASH4002", "금액은 음수일 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build();
    }
}