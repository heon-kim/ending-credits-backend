package com.hanaro.endingcredits.endingcreditsapi.utils.adapter;

import com.hanaro.endingcredits.endingcreditsapi.domain.will.dto.*;

public interface LLMPort {
    /**
     * 유언장의 취지 검증 및 생성 날짜 추출
     * @param content 대화 메시지 내용
     * @return 취지 및 생성 날짜
     */
    PurposeDto extractWillPurpose(String content);

    /**
     * 유언장의 재산 중 타입 관련 상속 정보 추출
     * @param content 대화 메시지 내용
     * @param type 상속 재산 타입
     *             - bank: 은행 관련
     *             - securities: 증권 관련
     *             - virtualAsset: 가상 자산
     *             - etc: 기타
     * @return 타입 관련 상속 정보
     */
    InheritanceDto[] extractWillInheritance(String content, String type);

    /**
     * 유언장의 수행자 정보 추출
     * @param content 대화 메시지 내용
     * @return 수행자 정보
     */
    ExecutorDto[] extractWIllExecutor(String content);

    /**
     * 유언장의 남기고 싶은 말 추출
     * @param content 대화 메시지 내용
     * @return 남기고 싶은 말
     */
    FinalMessageDto[] extractWIllFinalMessage(String content);

    /**
     * 유언장의 법적 유효성 추출
     * @param content 대화 메시지 내용
     * @return 법적 유효성
     */
    Boolean extractWillConfirmation(String content);

    /**
     * OCR 기반 생성 유언장 추출
     * @param content OCR 기반 생성 유언장 정보
     * @return 유언장 정보
     */
    WillDto extractWillByOCR(String content);
}