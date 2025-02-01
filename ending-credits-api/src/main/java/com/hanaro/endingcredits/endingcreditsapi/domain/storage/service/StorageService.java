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

    public String[] uploadFiles(MultipartFile[] files) throws IOException {
        String[] fileUrls = new String[files.length];
        int index = 0;
        for (MultipartFile file : files) {
            fileUrls[index++] = storagePort.uploadFile(file);
        }
        return fileUrls;
    }
}
