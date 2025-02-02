package com.hanaro.endingcredits.endingcreditsapi.utils.adapter;

import com.hanaro.endingcredits.endingcreditsapi.domain.auth.dto.IdCardDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface OCRPort {
    /**
     * 신분증을 인식하여 정보 추출
     * 신분증: 주민등록증, 운전면허증
     * @param file 신분증 이미지 파일
     * @return 이름, 주소, 주민등록번호
     */
    IdCardDto recognizeIdCard(MultipartFile file);

    /**
     * 유언장을 인식하여 정보 추출
     * @param filePaths 다중 이미지 파일 경로
     * @return 변환된 유언장 텍스트
     */
    String recognizeWill(List<String> filePaths);
}
