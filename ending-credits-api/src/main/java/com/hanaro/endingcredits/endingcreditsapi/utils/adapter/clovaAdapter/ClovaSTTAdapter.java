package com.hanaro.endingcredits.endingcreditsapi.utils.adapter.clovaAdapter;

import com.hanaro.endingcredits.endingcreditsapi.utils.adapter.STTPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClovaSTTAdapter implements STTPort {

    private final ClovaSTTProvider clovaSTTProvider;

    @Override
    public String transferSpeechToText(String filePath) {
        return clovaSTTProvider.transferSpeechToText(filePath);
    }
}
