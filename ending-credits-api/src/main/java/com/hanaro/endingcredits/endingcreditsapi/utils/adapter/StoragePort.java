package com.hanaro.endingcredits.endingcreditsapi.utils.adapter;

import org.springframework.web.multipart.MultipartFile;

public interface StoragePort {
    /**
     * 음성 파일 업로드
     * @param file 음성 파일
     * @return 음성 파일 저장 경로
     */
    String uploadFile(MultipartFile file);
}
