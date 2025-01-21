package com.hanaro.endingcredits.endingcreditsapi.domain.storage.service;

import com.hanaro.endingcredits.endingcreditsapi.utils.adapter.StoragePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StorageService {

    private final StoragePort storagePort;

    public String uploadFile(MultipartFile file) throws IOException {
        return storagePort.uploadFile(file);
    }

}
