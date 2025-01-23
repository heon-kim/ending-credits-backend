package com.hanaro.endingcredits.endingcreditsapi.domain.storage.controller;

import com.hanaro.endingcredits.endingcreditsapi.domain.storage.service.StorageService;
import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.ApiResponseEntity;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/upload")
public class StorageController {

    private final StorageService storageService;

    @Operation(summary = "원격 저장소에 파일 업로드")
    @PostMapping()
    public ApiResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String fileUrl = storageService.uploadFile(file);
            return ApiResponseEntity.onSuccess(fileUrl);
        } catch (IOException e) {
            return ApiResponseEntity.onFailure("UPLOAD4001", "파일 업로드에 실패했습니다.", null);
        }

    }

}
