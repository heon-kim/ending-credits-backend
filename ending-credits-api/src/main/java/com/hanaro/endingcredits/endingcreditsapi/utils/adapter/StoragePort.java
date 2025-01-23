package com.hanaro.endingcredits.endingcreditsapi.utils.adapter;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface StoragePort {
    /**
     * 원격 저장소에 파일 업로드
     * @param file 파일
     * @return 파일 저장 경로
     */
    String uploadFile(MultipartFile file) throws IOException;
}
