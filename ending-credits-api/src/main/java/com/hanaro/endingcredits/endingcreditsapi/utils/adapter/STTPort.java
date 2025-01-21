package com.hanaro.endingcredits.endingcreditsapi.utils.adapter;

public interface STTPort {
    /**
     * 음성 파일을 텍스트로 변환
     * @param filePath 음성 파일 경로
     * @return 변환된 텍스트
     */
    String transferSpeechToText(String filePath);
}
