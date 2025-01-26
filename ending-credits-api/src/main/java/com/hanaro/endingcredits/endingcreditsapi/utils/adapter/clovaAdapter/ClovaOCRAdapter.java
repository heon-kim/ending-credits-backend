package com.hanaro.endingcredits.endingcreditsapi.utils.adapter.clovaAdapter;

import com.hanaro.endingcredits.endingcreditsapi.domain.auth.dto.IdCardDto;
import com.hanaro.endingcredits.endingcreditsapi.utils.adapter.OCRPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class ClovaOCRAdapter implements OCRPort {

    private final ClovaOCRProvider clovaOCRProvider;

    @Override
    public IdCardDto recognizeIdCard(MultipartFile file) {
        return clovaOCRProvider.recognizeIdCard(file);
    }
}
