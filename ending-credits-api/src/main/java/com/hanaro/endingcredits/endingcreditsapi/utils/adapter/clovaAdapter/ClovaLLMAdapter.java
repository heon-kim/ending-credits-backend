package com.hanaro.endingcredits.endingcreditsapi.utils.adapter.clovaAdapter;

import com.hanaro.endingcredits.endingcreditsapi.domain.will.dto.ExecutorDto;
import com.hanaro.endingcredits.endingcreditsapi.domain.will.dto.FinalMessageDto;
import com.hanaro.endingcredits.endingcreditsapi.domain.will.dto.InheritanceDto;
import com.hanaro.endingcredits.endingcreditsapi.domain.will.dto.PurposeDto;
import com.hanaro.endingcredits.endingcreditsapi.utils.adapter.LLMPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClovaLLMAdapter implements LLMPort {

    private final ClovaLLMProvider clovaLLMProvider;

    @Override
    public PurposeDto extractWillPurpose(String content) {
        return clovaLLMProvider.extractWillPurpose(content);
    }

    @Override
    public InheritanceDto[] extractWillInheritance(String content, String type) {
        return clovaLLMProvider.extractWillInheritance(content, type);
    }

    @Override
    public ExecutorDto[] extractWIllExecutor(String content) {
        return clovaLLMProvider.extractWIllExecutor(content);
    }

    @Override
    public FinalMessageDto[] extractWIllFinalMessage(String content) {
        return clovaLLMProvider.extractWIllFinalMessage(content);
    }

    @Override
    public Boolean extractWillConfirmation(String content) {
        return clovaLLMProvider.extractWillConfirmation(content);
    }

}
