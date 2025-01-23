package com.hanaro.endingcredits.endingcreditsapi.domain.will.service;


import com.hanaro.endingcredits.endingcreditsapi.domain.will.dto.ExecutorDto;
import com.hanaro.endingcredits.endingcreditsapi.domain.will.dto.FinalMessageDto;
import com.hanaro.endingcredits.endingcreditsapi.domain.will.dto.InheritanceDto;
import com.hanaro.endingcredits.endingcreditsapi.domain.will.dto.PurposeDto;
import com.hanaro.endingcredits.endingcreditsapi.utils.adapter.LLMPort;
import com.hanaro.endingcredits.endingcreditsapi.utils.adapter.STTPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WillService {

    private final LLMPort llmPort;
    private final STTPort sttPort;

    public PurposeDto extractWillPurpose(String fileUrl) {
        String content = sttPort.transferSpeechToText(fileUrl);
        return llmPort.extractWillPurpose(content);
    }

    public InheritanceDto[] extractWillInheritance(String fileUrl, String type) {
        String content = sttPort.transferSpeechToText(fileUrl);
        return llmPort.extractWillInheritance(content, type);
    }

    public ExecutorDto[] extractWillExecutor(String fileUrl) {
        String content = sttPort.transferSpeechToText(fileUrl);
        return llmPort.extractWIllExecutor(content);
    }

    public FinalMessageDto[] extractWillFinalMessage(String fileUrl) {
        String content = sttPort.transferSpeechToText(fileUrl);
        return llmPort.extractWIllFinalMessage(content);
    }

    public Boolean extractWillConfirmation(String fileUrl) {
        String content = sttPort.transferSpeechToText(fileUrl);
        return llmPort.extractWillConfirmation(content);
    }
}
