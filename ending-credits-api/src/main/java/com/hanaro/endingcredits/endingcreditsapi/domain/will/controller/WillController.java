package com.hanaro.endingcredits.endingcreditsapi.domain.will.controller;

import com.hanaro.endingcredits.endingcreditsapi.domain.will.dto.*;
import com.hanaro.endingcredits.endingcreditsapi.domain.will.service.WillService;
import com.hanaro.endingcredits.endingcreditsapi.utils.apiPayload.ApiResponseEntity;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/will")
public class WillController {
    private final WillService willService;

    @Operation(summary = "유언장 조회")
    @GetMapping("")
    public ApiResponseEntity<WillInfoDto> getWillInfo(@AuthenticationPrincipal UUID memberId) {
        WillInfoDto willInfo = willService.getWillInfo(memberId);
        if (willInfo == null) {
            return ApiResponseEntity.onNoContentSuccess();
        }
        return ApiResponseEntity.onSuccess(willInfo);
    }

    @Operation(summary = "유언장 생성")
    @PostMapping("")
    public ApiResponseEntity<Void> createWillInfo(@AuthenticationPrincipal UUID memberId, @RequestBody WillInfoDto willinfoDto) {
        willService.createWillInfo(memberId, willinfoDto);
        return ApiResponseEntity.onSuccess(null);
    }

    @Operation(summary = "step1. 취지")
    @PostMapping("/speech/purpose")
    public ApiResponseEntity<PurposeDto> extractWillPurpose(@RequestParam String fileUrl) {
        PurposeDto purpose = willService.extractWillPurpose(fileUrl);
        return ApiResponseEntity.onSuccess(purpose);
    }

    @Operation(summary = "step2. 상속 재산")
    @PostMapping("/speech/inheritance")
    public ApiResponseEntity<InheritanceDto[]> extractWillInheritance(@RequestParam String fileUrl, @RequestParam String type) {
        InheritanceDto[] inheritances = willService.extractWillInheritance(fileUrl, type);
        return ApiResponseEntity.onSuccess(inheritances);
    }

    @Operation(summary = "step3. 집행자")
    @PostMapping("/speech/executor")
    public ApiResponseEntity<ExecutorDto[]> extractWillExecutor(@RequestParam String fileUrl) {
        ExecutorDto[] executors = willService.extractWillExecutor(fileUrl);
        return ApiResponseEntity.onSuccess(executors);
    }

    @Operation(summary = "step4. 남기고 싶은 말")
    @PostMapping("/speech/final-message")
    public ApiResponseEntity<FinalMessageDto[]> extractWillFinalMessage(@RequestParam String fileUrl) {
        FinalMessageDto[] finalMessages = willService.extractWillFinalMessage(fileUrl);
        return ApiResponseEntity.onSuccess(finalMessages);
    }

    @Operation(summary = "step5. 법적 효력")
    @PostMapping("/speech/confirmation")
    public ApiResponseEntity<Boolean> extractWillConfirmation(@RequestParam String fileUrl) {
        Boolean confirmation = willService.extractWillConfirmation(fileUrl);
        return ApiResponseEntity.onSuccess(confirmation);
    }

    @Operation(summary = "자필로 유언장 생성")
    @PostMapping("/ocr")
    public ApiResponseEntity<WillDto> extractWillByOCR(@RequestParam List<String> fileUrls) {
        WillDto will = willService.extractWillByOCR(fileUrls);
        return ApiResponseEntity.onSuccess(will);
    }
}
