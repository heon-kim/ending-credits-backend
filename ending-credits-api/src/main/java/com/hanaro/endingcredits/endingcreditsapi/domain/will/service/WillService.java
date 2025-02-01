package com.hanaro.endingcredits.endingcreditsapi.domain.will.service;


import com.hanaro.endingcredits.endingcreditsapi.domain.member.entities.MemberEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.member.service.MemberService;
import com.hanaro.endingcredits.endingcreditsapi.domain.will.dto.*;
import com.hanaro.endingcredits.endingcreditsapi.domain.will.entities.WillEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.will.entities.WillFileEntity;
import com.hanaro.endingcredits.endingcreditsapi.domain.will.repository.WillFileRepository;
import com.hanaro.endingcredits.endingcreditsapi.domain.will.repository.WillRepository;
import com.hanaro.endingcredits.endingcreditsapi.utils.adapter.LLMPort;
import com.hanaro.endingcredits.endingcreditsapi.utils.adapter.OCRPort;
import com.hanaro.endingcredits.endingcreditsapi.utils.adapter.STTPort;
import com.hanaro.endingcredits.endingcreditsapi.utils.mapper.WillMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WillService {

    private final MemberService memberService;
    private final WillRepository willRepository;
    private final WillFileRepository willFileRepository;
    private final WillMapper willMapper;

    private final LLMPort llmPort;
    private final STTPort sttPort;
    private final OCRPort ocrPort;

    public WillInfoDto getWillInfo(UUID memberId) {
        MemberEntity member = memberService.getMember(memberId);
        WillEntity willEntity = willRepository.findByMember(member);
        return willMapper.toWillInfoDto(willEntity);
    }

    @Transactional
    public void createWillInfo(UUID memberId, WillInfoDto willInfoDto) {
        MemberEntity member = memberService.getMember(memberId);

        WillEntity willEntity = willMapper.toWillEntity(willInfoDto);
        willEntity.setMember(member);
        willEntity = willRepository.save(willEntity);

        for (String fileUrl : willInfoDto.getFiles()) {
            WillFileEntity willFileEntity = WillFileEntity.builder()
                    .will(willEntity)
                    .fileUrl(fileUrl)
                    .build();
            willFileRepository.save(willFileEntity);
        }
    }

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

    public WillDto extractWillByOCR(List<String> fileUrls) {
        String will = ocrPort.recognizeWill(fileUrls);
        return llmPort.extractWillByOCR(will);
    }
}
