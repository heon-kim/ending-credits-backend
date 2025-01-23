package com.hanaro.endingcredits.endingcreditsapi.utils.adapter.clovaAdapter;

import com.hanaro.endingcredits.endingcreditsapi.utils.adapter.StoragePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class ClovaStorageAdapter implements StoragePort {

    private final ClovaStorageProvider clovaStorageProvider;

    @Override
    public String uploadFile(MultipartFile file) throws IOException {
        return clovaStorageProvider.uploadFile(file);
    }
}
